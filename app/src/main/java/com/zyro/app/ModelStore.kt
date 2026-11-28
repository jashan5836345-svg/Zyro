package com.zyro.app

import android.content.Context
import android.net.ConnectivityManager
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipInputStream

/**
 * Downloads the offline voice model ONCE, only on an unmetered network (Wi-Fi), so no mobile data is spent.
 * Model list and sizes: https://alphacephei.com/vosk/models  (check the file name there if the link ever changes).
 */
object ModelStore {

    /** Small Hindi model, about 42 MB. For Indian English use vosk-model-small-en-in-0.4 (about 36 MB). */
    const val VOSK_HI = "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip"

    @Volatile private var busy = false

    fun downloadVosk(ctx: Context, progress: (String) -> Unit, done: (Boolean, String) -> Unit) {
        if (busy) {
            done(false, "Already downloading.")
            return
        }
        val app = ctx.applicationContext
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm.activeNetwork == null) {
            done(false, "No internet. Connect to Wi-Fi and try again.")
            return
        }
        if (cm.isActiveNetworkMetered) {
            done(false, "Connect to Wi-Fi first: the download is about 42 MB and Zyro will not use mobile data for it.")
            return
        }
        busy = true
        Thread {
            val zip = File(app.cacheDir, "vosk-model.zip")
            val tmp = File(app.filesDir, "models/vosk_tmp")
            try {
                val conn = URL(VOSK_HI).openConnection() as HttpURLConnection
                conn.connectTimeout = 15_000
                conn.readTimeout = 30_000
                val total = conn.contentLengthLong
                var got = 0L
                var lastShown = 0L
                conn.inputStream.use { input ->
                    zip.outputStream().use { out ->
                        val buf = ByteArray(64 * 1024)
                        while (true) {
                            val n = input.read(buf)
                            if (n < 0) break
                            out.write(buf, 0, n)
                            got += n
                            if (got - lastShown > 8L * 1024 * 1024) {
                                lastShown = got
                                progress("Downloaded " + (got / 1048576) + " MB" + (if (total > 0) " of " + (total / 1048576) + " MB" else ""))
                            }
                        }
                    }
                }
                tmp.deleteRecursively()
                tmp.mkdirs()
                val root = tmp.canonicalPath + File.separator
                ZipInputStream(zip.inputStream().buffered()).use { zin ->
                    var e = zin.nextEntry
                    while (e != null) {
                        val name = e.name.substringAfter('/', "")
                        if (name.isNotEmpty()) {
                            val f = File(tmp, name)
                            if (!f.canonicalPath.startsWith(root)) throw SecurityException("Bad file in the download.")
                            if (e.isDirectory) {
                                f.mkdirs()
                            } else {
                                f.parentFile?.mkdirs()
                                f.outputStream().use { zin.copyTo(it) }
                            }
                        }
                        zin.closeEntry()
                        e = zin.nextEntry
                    }
                }
                if (!File(tmp, "am").isDirectory) throw IllegalStateException("The model files are incomplete.")
                val dest = OfflineStt.modelDir(app)
                dest.deleteRecursively()
                dest.parentFile?.mkdirs()
                if (!tmp.renameTo(dest)) throw IllegalStateException("Could not save the model.")
                zip.delete()
                busy = false
                done(true, "Offline voice is ready. Zyro now understands you without internet.")
            } catch (e: Exception) {
                tmp.deleteRecursively()
                zip.delete()
                busy = false
                done(false, "Download failed: " + (e.message ?: e.javaClass.simpleName))
            }
        }.start()
    }
}
