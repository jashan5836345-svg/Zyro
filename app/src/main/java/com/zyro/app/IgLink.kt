package com.zyro.app

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * "Instagram pe virat.kohli ki profile kholo" / "@username ki profile dikhao": opens the profile straight from the link
 * instagram://user?username=NAME, no searching and no AI. Works only when the sentence names ONE clear username
 * (letters, digits, dot, underscore). Anything else (a spoken name with spaces, "meri profile") goes on to the normal agent.
 */
object IgLink {

    private val USER = Regex("^[a-z0-9._]{3,30}$")

    private val NOISE = setOf(
        "instagram", "insta", "profile", "account", "id", "username", "user", "handle", "page", "ki", "ka", "ke", "ko", "par", "pe", "mein", "me", "se",
        "on", "in", "to", "the", "open", "kholo", "khol", "kholdo", "do", "dikhao", "dikha", "dikhado", "dekho", "dekhna", "show", "go", "jao", "ja",
        "wali", "wala", "wale", "ek", "zara", "please", "plz", "zyro", "hey", "ok", "ab", "aur", "bhai", "ye", "wo", "meri", "mera", "mere", "apni",
        "apna", "my", "mujhe", "mujhko", "link", "ye", "wahan", "ki.", "profile."
    )

    fun handle(ctx: Context, hands: Hands, raw: String): String? {
        val low = raw.lowercase()
        if (!(low.contains("instagram") || low.contains("insta") || low.contains("इंस्टा"))) return null
        val words = low.split(Regex("\\s+")).map { w -> w.trim { it in ",.!?।\"'()" } }.filter { it.isNotBlank() }
        val verb = listOf("kholo", "khol", "open", "dikhao", "dikha", "dekho", "show", "खोलो", "खोल", "दिखाओ", "दिखा", "देखो").any { low.contains(it) }
        val prof = listOf("profile", "account", "username", "handle", "प्रोफाइल", "प्रोफ़ाइल", "अकाउंट", "आईडी").any { low.contains(it) } ||
            low.contains("@") || words.any { it == "id" }
        if (!verb || !prof) return null

        val at = Regex("@([a-z0-9._]{3,30})").find(low)?.groupValues?.get(1)
        val user = (at ?: words.map { it.removePrefix("@") }
            .filter { it.isNotBlank() && it !in NOISE && USER.matches(it) }
            .let { if (it.size == 1) it[0] else null })?.trim('.') ?: return null
        if (!USER.matches(user)) return null

        return try {
            hands.openActivity(Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=$user")).setPackage("com.instagram.android"))
            "@$user की Instagram प्रोफाइल खोल दी।"
        } catch (e: Exception) {
            try {
                hands.openActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/$user/")))
                "Instagram ऐप नहीं मिला, इसलिए @$user की प्रोफाइल ब्राउज़र में खोल दी।"
            } catch (e2: Exception) {
                null
            }
        }
    }
}
