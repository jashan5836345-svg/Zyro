package com.zyro.app

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.net.HttpURLConnection
import java.net.URL

/** Voice entry: "key kaise banate hain", "api key setup" opens the guide. */
object KeyGuru {
    fun handle(ctx: Context, t: String): String? {
        fun has(vararg w: String) = w.any { t.contains(Local.strip(it)) }
        val ask = has("api key", "gemini key", "groq key", "cerebras key", "key setup", "key guru", "key kaise", "key banao", "key lagao", "key daalo", "की सेटअप", "एपीआई की")
        if (!ask) return null
        return try {
            ctx.startActivity(Intent(ctx, KeyGuruActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            "ठीक है, key गाइड खोल रहा हूँ। ऊपर वाला बटन दबाओ, key कॉपी करो, फिर पेस्ट बटन दबाओ। मैं खुद जाँच कर लूँगा।"
        } catch (e: Exception) {
            "गाइड नहीं खुल पाई। Settings में 'Key कैसे बनाएँ' बटन दबाओ।"
        }
    }
}

/**
 * A step-by-step guide for people who have never made an API key: it opens the right page, tells what to press,
 * reads the copied key from the clipboard, finds out which service it belongs to, tests it for real and saves it.
 * The key stays on the phone.
 */
class KeyGuruActivity : Activity() {

    private companion object {
        val BG = Color.rgb(2, 11, 18)
        val GOLD = Color.rgb(60, 210, 250)
        val SOFT = Color.rgb(222, 236, 255)
        val DIM = Color.rgb(150, 150, 165)
        val GOOD = Color.rgb(90, 220, 130)
        val BAD = Color.rgb(255, 120, 110)
    }

    private class Prov(val id: Int, val name: String, val why: String, val url: String, val steps: String, val prefix: String, val speak: String)

    private val provs = listOf(
        Prov(
            0, "Gemini", "ज़रूरी: लाइव बातचीत, फोटो और स्क्रीन देखना।", "https://aistudio.google.com/apikey",
            "1) नीचे 'पेज खोलो' दबाओ।\n2) अपने Google अकाउंट से लॉगिन करो।\n3) 'Create API key' दबाओ (पूछे तो नया प्रोजेक्ट चुनो)।\n4) बनी हुई key के पास कॉपी वाला आइकन दबाओ।\n5) वापस यहाँ आकर 'कॉपी की हुई key लगाओ' दबाओ।",
            "AIza",
            "Gemini key ऐसे बनाओ। पहला, पेज खोलो बटन दबाओ। दूसरा, अपने Google अकाउंट से लॉगिन करो। तीसरा, Create API key दबाओ। चौथा, key के पास कॉपी आइकन दबाओ। पाँचवाँ, वापस ज़ायरो में आकर कॉपी की हुई key लगाओ बटन दबाओ।"
        ),
        Prov(
            1, "Groq", "तेज़ जवाब: चैट और एजेंट बहुत तेज़ चलते हैं।", "https://console.groq.com/keys",
            "1) 'पेज खोलो' दबाओ।\n2) Google से साइन अप या लॉगिन करो।\n3) 'Create API Key' दबाओ, नाम में zyro लिखो।\n4) key तभी दिखती है, इसलिए तुरंत कॉपी करो।\n5) वापस यहाँ आकर 'कॉपी की हुई key लगाओ' दबाओ।",
            "gsk_",
            "Groq key ऐसे बनाओ। पेज खोलो बटन दबाओ, Google से लॉगिन करो, Create API Key दबाओ, key को तुरंत कॉपी करो, फिर वापस आकर कॉपी की हुई key लगाओ बटन दबाओ।"
        ),
        Prov(
            2, "Cerebras", "वैकल्पिक: Groq की जगह दूसरा तेज़ सहारा।", "https://cloud.cerebras.ai",
            "1) 'पेज खोलो' दबाओ।\n2) साइन अप या लॉगिन करो।\n3) 'API Keys' में जाकर नई key बनाओ।\n4) key कॉपी करो।\n5) वापस यहाँ आकर 'कॉपी की हुई key लगाओ' दबाओ।",
            "csk-",
            "Cerebras key ऐसे बनाओ। पेज खोलो बटन दबाओ, लॉगिन करो, API Keys में नई key बनाओ, कॉपी करो, फिर वापस आकर कॉपी की हुई key लगाओ बटन दबाओ।"
        )
    )

    private val status = HashMap<Int, TextView>()
    private lateinit var manual: EditText
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = BG
        window.navigationBarColor = BG
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(BG)
            setPadding(dp(18), dp(34), dp(18), dp(28))
        }

        fun text(s: String, size: Float, color: Int, top: Int = 0) = TextView(this).apply {
            this.text = s
            textSize = size
            setTextColor(color)
            setPadding(0, dp(top), 0, dp(4))
        }

        fun button(label: String, filled: Boolean = false, action: () -> Unit) = Button(this).apply {
            text = label
            isAllCaps = false
            setTextColor(if (filled) BG else SOFT)
            setBackgroundColor(if (filled) GOLD else Color.rgb(16, 34, 48))
            gravity = Gravity.CENTER
            setOnClickListener { action() }
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.topMargin = dp(8)
            layoutParams = lp
        }

        box.addView(text("AI की key लगाओ (मुफ़्त, 2 मिनट)", 22f, GOLD))
        box.addView(text("Zyro को सोचने के लिए एक मुफ़्त 'key' चाहिए। ये तुम्हारे अपने अकाउंट से बनती है, इसमें पैसे नहीं लगते, और सिर्फ़ तुम्हारे फोन में रहती है। नीचे हर कदम लिखा है।", 14f, SOFT, 8))

        box.addView(text("सबसे आसान तरीका", 16f, GOLD, 18))
        box.addView(text("कोई भी key कॉपी करो, फिर ये बटन दबाओ। मैं खुद पहचान लूँगा कि वो किसकी है, जाँच लूँगा और सेव कर दूँगा।", 13f, DIM))
        box.addView(button("कॉपी की हुई key लगाओ (अपने आप पहचान)", filled = true) { autoSave(clip()) })

        for (p in provs) {
            box.addView(text("${p.name} key", 18f, GOLD, 22))
            box.addView(text(p.why, 13f, DIM))
            val st = text("", 14f, DIM)
            status[p.id] = st
            box.addView(st)
            box.addView(text(p.steps, 14f, SOFT, 4))
            box.addView(button("1. पेज खोलो") { openPage(p.url) })
            box.addView(button("2. कॉपी की हुई key लगाओ") { saveFor(p, clip()) })
            box.addView(button("ज़ायरो बोलकर समझाए") { speak(p.speak) })
        }

        box.addView(text("या key खुद यहाँ पेस्ट करो", 16f, GOLD, 24))
        manual = EditText(this).apply {
            hint = "key यहाँ पेस्ट करो"
            setHintTextColor(DIM)
            setTextColor(SOFT)
            setSingleLine(true)
        }
        box.addView(manual)
        box.addView(button("जाँचो और सेव करो") { autoSave(manual.text.toString()) })
        box.addView(text("सुरक्षा: key तुम्हारे फोन में ही रहती है। किसी को भेजना मत, और चैट या फोटो में दिखाना मत।", 12f, DIM, 18))

        setContentView(ScrollView(this).apply {
            setBackgroundColor(BG)
            addView(box)
        })
        refresh()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        for (p in provs) {
            val have = when (p.id) {
                0 -> Prefs.geminiKeysRaw.isNotBlank()
                1 -> Prefs.groqKey.isNotBlank()
                else -> Prefs.cerebrasKey.isNotBlank()
            }
            setStatus(p, if (have) "✅ key लगी हुई है" else "❌ अभी key नहीं है", if (have) GOOD else DIM)
        }
    }

    private fun setStatus(p: Prov, s: String, color: Int) {
        status[p.id]?.let { it.text = s; it.setTextColor(color) }
    }

    private fun speak(s: String) {
        try { Assistant.get(this).announce(s) } catch (e: Exception) { Toast.makeText(this, s, Toast.LENGTH_LONG).show() }
    }

    private fun openPage(url: String) {
        try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) } catch (e: Exception) { Toast.makeText(this, "ब्राउज़र नहीं खुला। ये पता Chrome में खोलो: $url", Toast.LENGTH_LONG).show() }
    }

    private fun clip(): String {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return try { cm.primaryClip?.getItemAt(0)?.coerceToText(this)?.toString()?.trim().orEmpty() } catch (e: Exception) { "" }
    }

    private fun clean(raw: String) = raw.trim().replace("\n", "").replace(" ", "").replace("\"", "")

    /** Which service a key belongs to, by its start (Gemini AIza, Groq gsk_, Cerebras csk-). */
    private fun guess(key: String): Prov? = provs.firstOrNull { key.startsWith(it.prefix) }

    /** HTTP code of a harmless call that needs the key: 200 = good, 429 = good but busy, 400/401/403 = wrong, 0 = no internet. */
    private fun check(id: Int, key: String): Int {
        val url = when (id) {
            0 -> "https://generativelanguage.googleapis.com/v1beta/models?pageSize=1&key=$key"
            1 -> "https://api.groq.com/openai/v1/models"
            else -> "https://api.cerebras.ai/v1/models"
        }
        val c = URL(url).openConnection() as HttpURLConnection
        return try {
            c.connectTimeout = 10000
            c.readTimeout = 10000
            c.setRequestProperty("User-Agent", "Zyro")
            if (id != 0) c.setRequestProperty("Authorization", "Bearer $key")
            c.responseCode
        } catch (e: Exception) {
            0
        } finally {
            c.disconnect()
        }
    }

    private fun save(p: Prov, key: String) {
        when (p.id) {
            0 -> {
                val cur = Prefs.geminiKeysRaw
                if (!cur.contains(key)) Prefs.geminiKeysRaw = if (cur.isBlank()) key else cur.trim().trimEnd(',') + "," + key
            }
            1 -> Prefs.groqKey = key
            else -> Prefs.cerebrasKey = key
        }
    }

    private fun good(code: Int) = code == 200 || code == 429

    private fun saveFor(forced: Prov, raw: String) {
        val key = clean(raw)
        if (key.length < 20) { speak("कॉपी की हुई key नहीं मिली। पहले key कॉपी करो, फिर ये बटन दबाओ।"); return }
        val p = guess(key) ?: forced
        if (p.id != forced.id) toast("ये तो ${p.name} की key है, उसी में लगा रहा हूँ।")
        setStatus(p, "जाँच रहा हूँ…", DIM)
        Thread {
            val code = check(p.id, key)
            runOnUiThread { done(p, key, code) }
        }.start()
    }

    private fun autoSave(raw: String) {
        val key = clean(raw)
        if (key.length < 20) { speak("कॉपी की हुई key नहीं मिली। पहले key कॉपी करो, फिर ये बटन दबाओ।"); return }
        toast("जाँच रहा हूँ…")
        Thread {
            var chosen: Prov? = guess(key)
            var code = if (chosen != null) check(chosen.id, key) else -1
            if (chosen == null) {
                for (p in provs) {
                    val c = check(p.id, key)
                    if (good(c)) { chosen = p; code = c; break }
                    code = c
                }
            }
            val pick = chosen
            runOnUiThread {
                if (pick == null) {
                    speak("ये key किसी सही सेवा की नहीं लगी। पूरी key कॉपी करो, बिना खाली जगह के, और फिर कोशिश करो।")
                    toast("key पहचान में नहीं आई (कोड $code)")
                } else done(pick, key, code)
            }
        }.start()
    }

    private fun done(p: Prov, key: String, code: Int) {
        when {
            good(code) -> {
                save(p, key)
                refresh()
                speak("${p.name} की key लग गई" + if (code == 429) "। अभी इसकी मुफ़्त लिमिट भरी है, थोड़ी देर बाद चलेगी।" else "। अब मैं तैयार हूँ।")
            }
            code == 0 -> { setStatus(p, "❌ इंटरनेट नहीं चला", BAD); speak("इंटरनेट नहीं चल रहा, थोड़ी देर बाद फिर कोशिश करो।") }
            code == 400 || code == 401 || code == 403 -> { setStatus(p, "❌ key गलत है", BAD); speak("ये ${p.name} की सही key नहीं लगी। पूरी key फिर से कॉपी करो, बिना खाली जगह के।") }
            else -> { setStatus(p, "❌ जाँच नहीं हो पाई ($code)", BAD); speak("जाँच नहीं हो पाई, थोड़ी देर बाद फिर कोशिश करो।") }
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}
