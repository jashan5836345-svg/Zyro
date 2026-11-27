package com.zyro.app

import android.content.Context
import java.io.File
import java.util.Locale

/**
 * Catches the wake name even when the microphone spells it wrongly (Sayro, Zairo, Cairo, Jyro, Siro, ज़ायरो ...).
 * A heard word counts as the name when its sound has the shape  [z/j/s/c/x] + vowels + r + [o/u]  (Devanagari is turned
 * into Roman letters first). Ordinary words such as sir, sorry, sure, zara, sara, hero do not fit that shape.
 * Words the user adds by voice ("wake word jodo sid") are remembered in a small file and match exactly.
 */
object WakeFuzzy {

    private val DEV = mapOf(
        'ज' to "j", 'स' to "s", 'श' to "s", 'ष' to "s", 'र' to "r", 'य' to "y", 'क' to "k",
        'अ' to "a", 'आ' to "a", 'इ' to "i", 'ई' to "i", 'उ' to "u", 'ऊ' to "u", 'ए' to "e", 'ऐ' to "ai", 'ओ' to "o", 'औ' to "au",
        'ा' to "a", 'ि' to "i", 'ी' to "i", 'ु' to "u", 'ू' to "u", 'े' to "e", 'ै' to "ai", 'ो' to "o", 'ौ' to "au"
    )
    private val SHAPE = Regex("^[zjscx][aeiy]{0,3}r[ou]$")
    private val WORD = Regex("[\\p{L}\\p{M}\\p{N}]+")

    private fun roman(token: String): String {
        val sb = StringBuilder()
        for (ch in token.lowercase(Locale.ROOT)) {
            val m = DEV[ch]
            if (m != null) sb.append(m) else if (ch in 'a'..'z') sb.append(ch)
        }
        val out = StringBuilder()
        for (c in sb) if (out.isEmpty() || out[out.length - 1] != c) out.append(c)
        return out.toString()
    }

    fun looksLikeName(token: String): Boolean = token.length in 3..9 && SHAPE.matches(roman(token))

    private fun file(c: Context) = File(c.applicationContext.filesDir, "wake_aliases.txt")

    fun learned(c: Context): List<String> = try {
        val f = file(c)
        if (f.exists()) f.readLines().map { it.trim().lowercase(Locale.ROOT) }.filter { it.length >= 3 } else emptyList()
    } catch (e: Exception) { emptyList() }

    private fun saveLearned(c: Context, list: List<String>) {
        try { file(c).writeText(list.joinToString("\n")) } catch (e: Exception) { /* optional */ }
    }

    /** Finds the spoken name in [text]; returns (start, end) of that word in [text], or null. */
    fun find(c: Context, text: String): Pair<Int, Int>? {
        val extra = learned(c)
        for (m in WORD.findAll(text)) {
            val w = m.value
            if (looksLikeName(w) || w.lowercase(Locale.ROOT) in extra) return m.range.first to (m.range.last + 1)
        }
        return null
    }

    private val NOISE = setOf(
        "wake", "word", "वेक", "वर्ड", "वेकवर्ड", "jodo", "जोड़ो", "jod", "do", "दो", "add", "karo", "करो", "mein", "में", "ko", "को", "bhi", "भी",
        "naam", "नाम", "maano", "मानो", "samjho", "समझो", "hatao", "हटाओ", "remove", "delete", "kya", "क्या", "hain", "हैं", "hai", "है",
        "mera", "मेरा", "ka", "का", "ki", "की", "ye", "ये", "isse", "इसे", "list", "batao", "बताओ", "zyro", "hey", "ek", "aur", "और", "naya", "नया"
    ).map { Local.strip(it) }.toSet()

    /** "wake word jodo sid", "sid ko bhi naam maano", "wake word kya kya hain", "wake word hatao sid". */
    fun handle(ctx: Context, t: String): String? {
        fun has(vararg w: String) = w.any { t.contains(Local.strip(it)) }
        if (!has("wake word", "वेक वर्ड", "वेकवर्ड", "naam maano", "नाम मानो", "naam samjho", "नाम समझो")) return null
        if (has("band", "बंद", " off", "chalu", "चालू")) return null // switching the wake word on or off is handled elsewhere
        val adding = has("jodo", "जोड़ो", "jod do", "add", "maano", "मानो", "samjho", "समझो")
        val removing = has("hatao", "हटाओ", "remove", "delete")
        val cand = t.split(" ").filter { it.isNotBlank() && it !in NOISE && it.length >= 3 }.lastOrNull()
        val now = learned(ctx)
        if (removing) {
            if (cand == null || cand !in now) return "वो शब्द मेरी जोड़ी हुई सूची में नहीं है।"
            saveLearned(ctx, now.filter { it != cand })
            return "ठीक है, '$cand' हटा दिया।"
        }
        if (adding) {
            if (cand == null) return "कौन सा शब्द जोड़ूँ? जैसे बोलो: वेक वर्ड जोड़ो सिड।"
            if (cand in now) return "'$cand' पहले से जुड़ा है।"
            saveLearned(ctx, (now + cand).takeLast(30))
            return "ठीक है, अब '$cand' सुनकर भी मैं जाग जाऊँगा।"
        }
        val extra = if (now.isEmpty()) "" else " तुम्हारे जोड़े हुए शब्द: ${now.joinToString(", ")}।"
        return "ज़ायरो, सायरो, जैरो, ज़ीरो जैसी हर मिलती-जुलती आवाज़ से मैं जाग जाता हूँ।$extra"
    }
}
