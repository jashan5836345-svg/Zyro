package com.zyro.app

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import java.io.File

/**
 * Offline speech to text with Vosk (small Hindi model, about 42 MB, downloaded once over Wi-Fi by ModelStore).
 * It listens once, until you finish a sentence, and then gives the text to the caller. No internet is used.
 * Assistant.listen() calls it instead of Android's SpeechRecognizer when Prefs.offlineStt is on and the model exists.
 */
object OfflineStt {

    @Volatile private var model: Model? = null
    @Volatile private var gen = 0
    @Volatile private var running = false

    /** Folder holding the unpacked Vosk model (it contains the sub-folders am, conf and graph). */
    fun modelDir(ctx: Context): File = File(ctx.filesDir, "models/vosk")

    fun ready(ctx: Context): Boolean = File(modelDir(ctx), "am").isDirectory

    @Synchronized
    private fun load(ctx: Context): Model? {
        if (model == null) {
            model = try {
                Model(modelDir(ctx).absolutePath)
            } catch (e: Throwable) {
                null
            }
        }
        return model
    }

    /** Stop listening now. Safe to call any time, even when nothing is running. */
    fun cancel() {
        gen++
        running = false
    }

    private fun textOf(json: String, key: String): String = try {
        JSONObject(json).optString(key, "").trim()
    } catch (e: Exception) {
        ""
    }

    /**
     * Listen for one sentence. Exactly one of the three callbacks fires (unless cancel() was called):
     * onText = what you said, onNothing = silence or no words, onError = the model or microphone failed.
     * The callbacks run on a background thread: post to the main thread inside them.
     */
    fun listenOnce(
        ctx: Context,
        onText: (String) -> Unit,
        onNothing: () -> Unit,
        onError: (String) -> Unit,
        maxMs: Long = 12_000L,
        silenceMs: Long = 1_000L
    ) {
        cancel()
        val my = gen
        running = true
        Thread {
            var rec: AudioRecord? = null
            var recognizer: Recognizer? = null
            var outcome = 0 // 0 nothing, 1 text, 2 error
            var result = ""
            try {
                val m = load(ctx)
                if (m == null) {
                    outcome = 2
                    result = "Offline voice model is missing or broken."
                } else {
                    val sr = 16000
                    val minBuf = AudioRecord.getMinBufferSize(sr, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
                    val bufSize = maxOf(minBuf, sr) * 2
                    rec = AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, sr, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize)
                    if (rec.state != AudioRecord.STATE_INITIALIZED) {
                        outcome = 2
                        result = "The microphone is busy or not allowed."
                    } else {
                        recognizer = Recognizer(m, sr.toFloat())
                        rec.startRecording()
                        val data = ShortArray(1600) // 100 ms of sound
                        val start = System.currentTimeMillis()
                        var lastVoice = start
                        var heardSomething = false
                        while (running && my == gen && System.currentTimeMillis() - start < maxMs) {
                            val n = rec.read(data, 0, data.size)
                            if (n <= 0) continue
                            val now = System.currentTimeMillis()
                            if (recognizer.acceptWaveForm(data, n)) {
                                val t = textOf(recognizer.result, "text")
                                if (t.isNotEmpty()) {
                                    outcome = 1
                                    result = t
                                    break
                                }
                            } else {
                                val p = textOf(recognizer.partialResult, "partial")
                                if (p.isNotEmpty()) {
                                    heardSomething = true
                                    lastVoice = now
                                }
                            }
                            if (heardSomething && now - lastVoice > silenceMs) {
                                val t = textOf(recognizer.finalResult, "text")
                                if (t.isNotEmpty()) {
                                    outcome = 1
                                    result = t
                                }
                                break
                            }
                        }
                        if (outcome == 0 && heardSomething) {
                            val t = textOf(recognizer.finalResult, "text")
                            if (t.isNotEmpty()) {
                                outcome = 1
                                result = t
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                outcome = 2
                result = "Offline voice error: " + (e.message ?: e.javaClass.simpleName)
            } finally {
                try { rec?.stop() } catch (e: Throwable) { /* ignore */ }
                try { rec?.release() } catch (e: Throwable) { /* ignore */ }
                try { recognizer?.close() } catch (e: Throwable) { /* ignore */ }
            }
            if (my != gen) return@Thread // cancelled: say nothing
            running = false
            when (outcome) {
                1 -> onText(result)
                2 -> onError(result)
                else -> onNothing()
            }
        }.start()
    }
}
