package com.zyro.app

import android.os.Build

/**
 * Knows WHICH phone Zyro is running on (brand, model, Android version, skin such as One UI / HyperOS /
 * ColorOS) and gives the menu path for that brand, because the same setting has different names and places on
 * Samsung, Xiaomi, Oppo/Realme/OnePlus, Vivo, Pixel and others. Detected at runtime with android.os.Build and
 * system properties (read with a safe try/catch), so it adapts to every device the app is installed on.
 */
object DeviceProfile {

    class Info(
        val family: String,
        val brand: String,
        val model: String,
        val release: String,
        val sdk: Int,
        val skin: String
    )

    private var cached: Info? = null

    private fun prop(key: String): String = try {
        val c = Class.forName("android.os.SystemProperties")
        (c.getMethod("get", String::class.java).invoke(null, key) as? String) ?: ""
    } catch (e: Throwable) {
        ""
    }

    private fun detectFamily(m: String, b: String): String {
        val x = (m + " " + b).lowercase()
        return when {
            x.contains("samsung") -> "SAMSUNG"
            x.contains("xiaomi") || x.contains("redmi") || x.contains("poco") || prop("ro.miui.ui.version.name").isNotEmpty() || prop("ro.mi.os.version.name").isNotEmpty() -> "XIAOMI"
            x.contains("oppo") || x.contains("realme") || x.contains("oneplus") || prop("ro.build.version.oplusrom").isNotEmpty() -> "OPLUS"
            x.contains("vivo") || x.contains("iqoo") -> "VIVO"
            x.contains("google") -> "STOCK"
            x.contains("motorola") || x.contains("lenovo") -> "MOTO"
            x.contains("nothing") -> "NOTHING"
            x.contains("infinix") || x.contains("tecno") || x.contains("itel") || x.contains("transsion") -> "TRANSSION"
            x.contains("huawei") || x.contains("honor") -> "HUAWEI"
            else -> "STOCK"
        }
    }

    private fun detectSkin(family: String): String {
        return try {
            when (family) {
                "SAMSUNG" -> {
                    val v = prop("ro.build.version.oneui").toIntOrNull() ?: 0
                    if (v > 0) "One UI " + (v / 10000) + "." + ((v % 10000) / 100) else "One UI"
                }
                "XIAOMI" -> {
                    val hy = prop("ro.mi.os.version.name")
                    if (hy.isNotEmpty()) "HyperOS " + hy else "MIUI " + prop("ro.miui.ui.version.name")
                }
                "OPLUS" -> "ColorOS/OxygenOS " + prop("ro.build.version.oplusrom")
                "VIVO" -> (prop("ro.vivo.os.name").ifEmpty { "Funtouch/OriginOS" }) + " " + prop("ro.vivo.os.version")
                "STOCK" -> if (Build.MANUFACTURER.lowercase().contains("google")) "Pixel (stock Android)" else "near-stock Android"
                "MOTO" -> "Motorola (near-stock Android)"
                "NOTHING" -> "Nothing OS"
                "TRANSSION" -> "XOS/HiOS/itelOS"
                "HUAWEI" -> "EMUI/MagicOS/HarmonyOS"
                else -> ""
            }.trim()
        } catch (e: Throwable) {
            ""
        }
    }

    fun get(): Info {
        cached?.let { return it }
        val m = Build.MANUFACTURER ?: ""
        val b = Build.BRAND ?: ""
        val fam = detectFamily(m, b)
        val info = Info(fam, m.replaceFirstChar { it.uppercase() }, Build.MODEL ?: "", Build.VERSION.RELEASE ?: "", Build.VERSION.SDK_INT, detectSkin(fam))
        cached = info
        return info
    }

    /** One short line: what phone this is. */
    fun describe(): String {
        val i = get()
        return "THIS PHONE: " + i.brand + " " + i.model + ", Android " + i.release + " (API " + i.sdk + ")" +
            (if (i.skin.isNotEmpty()) ", " + i.skin else "") + ". Use the menu names of THIS brand below."
    }

    private fun versionNote(sdk: Int): String {
        val sb = StringBuilder()
        if (sdk >= 33) sb.append("Android 13+: apps must be allowed to show notifications (Settings > Apps > app > Notifications), and a sideloaded APK needs Allow restricted settings (App info > three-dot menu) before Accessibility can be enabled. ")
        if (sdk >= 34) sb.append("Android 14+: photo access can be 'selected photos only'; exact alarms and full-screen alerts need their own permission. ")
        if (sdk >= 35) sb.append("Android 15+: Private space and app archiving exist (Settings > Security & privacy). ")
        if (sdk in 1..28) sb.append("This is an old Android version: menus are older and some features (gesture navigation, per-app battery controls) may be missing. ")
        return sb.toString().trim()
    }

    private val PATHS: Map<String, Map<String, String>> = mapOf(
        "STOCK" to mapOf(
            "wifi" to "Settings > Network & internet > Internet.",
            "bluetooth" to "Settings > Connected devices > Pair new device.",
            "hotspot" to "Settings > Network & internet > Hotspot & tethering > Wi-Fi hotspot.",
            "display" to "Settings > Display (Brightness, Dark theme, Smooth display = refresh rate, Screen timeout, Font size).",
            "sound" to "Settings > Sound & vibration.",
            "dnd" to "Settings > Sound & vibration > Do Not Disturb (newer versions: Modes).",
            "battery" to "Settings > Battery (Battery Saver, Battery usage, Adaptive charging); per app: Settings > Apps > the app > App battery usage > Unrestricted.",
            "storage" to "Settings > Storage; Files by Google > Clean.",
            "apps" to "Settings > Apps > See all apps > the app (Permissions, Notifications, Storage & cache); defaults: Settings > Apps > Default apps.",
            "notifications" to "Settings > Notifications > App settings (or long-press a notification > the gear).",
            "security" to "Settings > Security & privacy (Device unlock, Find Hub / Find My Device, Play Protect, Privacy, Permission manager).",
            "location" to "Settings > Location.",
            "update" to "Settings > System > Software updates > System update.",
            "developer" to "Settings > About phone > tap Build number 7 times; then Settings > System > Developer options.",
            "accessibility" to "Settings > Accessibility > Installed apps (or Downloaded apps) > Zyro.",
            "keepalive" to "Settings > Apps > Zyro > App battery usage > Unrestricted; Settings > Accessibility > Zyro ON; Display over other apps ON; Microphone, Contacts, Phone, SMS permissions as needed. Sideloaded app and Accessibility greyed out (Android 13+): Settings > Apps > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Power + Volume Down; screen record from the Quick Settings tile.",
            "restart" to "Hold Power > Restart; frozen phone: hold Power 10-30 seconds (Pixel: Power + Volume Up about 30 s). Reset options: Settings > System > Reset options."
        ),
        "SAMSUNG" to mapOf(
            "wifi" to "Settings > Connections > Wi-Fi.",
            "bluetooth" to "Settings > Connections > Bluetooth.",
            "hotspot" to "Settings > Connections > Mobile Hotspot and Tethering > Mobile Hotspot.",
            "display" to "Settings > Display (Brightness, Dark mode, Motion smoothness = refresh rate, Screen timeout, Font size and style, Eye comfort shield).",
            "sound" to "Settings > Sounds and vibration.",
            "dnd" to "Settings > Notifications > Do not disturb (or Modes and Routines > Modes).",
            "battery" to "Settings > Battery (older One UI: Battery and device care > Battery): Power saving, Background usage limits (Sleeping apps / Never sleeping apps), Protect battery (limits charging to about 85 percent).",
            "storage" to "Settings > Battery and device care > Storage (newer: Device care > Storage) and the My Files app.",
            "apps" to "Settings > Apps > the app (Permissions, Battery, Storage); defaults: Settings > Apps > Choose default apps.",
            "notifications" to "Settings > Notifications > App notifications.",
            "security" to "Settings > Security and privacy (Biometrics, Lock screen, Samsung Find / Google Find Hub, Auto Blocker). Auto Blocker can block installing apps from outside the stores: turn it off only if the user asks to install an APK.",
            "location" to "Settings > Location.",
            "update" to "Settings > Software update > Download and install.",
            "developer" to "Settings > About phone > Software information > tap Build number 7 times; then Settings > Developer options (bottom of Settings).",
            "accessibility" to "Settings > Accessibility > Installed apps > Zyro.",
            "keepalive" to "Settings > Apps > Zyro > Battery > Unrestricted; Settings > Battery > Background usage limits > turn OFF 'Put unused apps to sleep' and add Zyro to 'Never sleeping apps'; Accessibility > Installed apps > Zyro ON; Settings > Apps > Zyro > Permissions. Accessibility greyed out for a sideloaded app: Settings > Apps > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Side + Volume Down; or palm swipe (Settings > Advanced features > Motions and gestures); screen recorder tile in Quick Settings.",
            "restart" to "Hold Side + Volume Down > Restart (a long press of Side alone may open the assistant); frozen phone: Volume Down + Side for about 10 s."
        ),
        "XIAOMI" to mapOf(
            "wifi" to "Settings > Wi-Fi (top of Settings).",
            "bluetooth" to "Settings > Bluetooth.",
            "hotspot" to "Settings > Connection & sharing (or Portable hotspot) > Personal/Portable hotspot.",
            "display" to "Settings > Display (HyperOS: Display and brightness): Refresh rate, Dark mode, Reading mode, Text size.",
            "sound" to "Settings > Sound & vibration.",
            "dnd" to "Settings > Sound & vibration > Do not disturb (older MIUI: Notifications & Control center).",
            "battery" to "Settings > Battery (Battery saver); per app: Settings > Apps > Manage apps > the app > Battery saver > No restrictions; Security app > Battery.",
            "storage" to "Settings > About phone > Storage (or Settings > Storage); Security app > Cleaner.",
            "apps" to "Settings > Apps > Manage apps > the app; permissions: Settings > Apps > Permissions.",
            "notifications" to "Settings > Notifications & Control center > App notifications.",
            "security" to "Settings > Passwords & security (Screen lock, Face unlock, Find device) and Privacy protection.",
            "location" to "Settings > Location (or Privacy protection > Location).",
            "update" to "Settings > About phone > the OS/HyperOS version box > Check for updates.",
            "developer" to "Settings > About phone > tap the OS version (HyperOS/MIUI version) 7 times; then Settings > Additional settings > Developer options.",
            "accessibility" to "Settings > Additional settings > Accessibility > Downloaded services > Zyro.",
            "keepalive" to "Settings > Apps > Manage apps > Zyro > turn ON Autostart; Battery saver > No restrictions; Other permissions > Display pop-up windows while running in the background ON (needed on some versions); in Recents long-press the Zyro card > Lock (padlock) so the system does not kill it. Accessibility greyed out for a sideloaded app: Manage apps > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Power + Volume Down, or swipe down with three fingers; screen recorder app or the Control Center tile (Control Center = swipe down from the top right).",
            "restart" to "Hold Power > Restart; frozen phone: hold Power 10-15 seconds."
        ),
        "OPLUS" to mapOf(
            "wifi" to "Settings > WLAN (Oppo calls Wi-Fi 'WLAN'; OnePlus: Wi-Fi).",
            "bluetooth" to "Settings > Bluetooth.",
            "hotspot" to "Settings > Personal hotspot (or Mobile network > Personal hotspot / Connection & sharing).",
            "display" to "Settings > Display & brightness (Screen refresh rate, Dark mode, Font size, Eye comfort).",
            "sound" to "Settings > Sound & vibration.",
            "dnd" to "Settings > Sound & vibration > Do Not Disturb.",
            "battery" to "Settings > Battery (Power saving, Battery health, Optimized charging); per app: Settings > Apps > App management > the app > Battery usage > Allow background activity.",
            "storage" to "Settings > About device > Storage (or Settings > Storage); Phone Manager > Clean.",
            "apps" to "Settings > Apps > App management > the app.",
            "notifications" to "Settings > Notifications & status bar > App notifications.",
            "security" to "Settings > Password & security (Lock screen password, Fingerprint, Face unlock) and Privacy (Permission manager).",
            "location" to "Settings > Location (or Privacy > Location).",
            "update" to "Settings > About device > Version (or System update) > check for update.",
            "developer" to "Settings > About device > Version > tap Build number 7 times; then Settings > Additional settings > Developer options.",
            "accessibility" to "Settings > Additional settings > Accessibility > Downloaded apps > Zyro.",
            "keepalive" to "Settings > Apps > App management > Zyro > Battery usage > Allow background activity, and Allow auto launch (Auto launch); lock Zyro in Recents (lock/more icon on its card); if Zyro stops listening with the screen off, in Settings > Battery turn off sleep-standby optimisation. Accessibility greyed out for a sideloaded app: App management > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Power + Volume Down, or swipe down with three fingers; screen recording from the Quick Settings tile.",
            "restart" to "Hold Power > Restart; frozen phone: hold Power + Volume Up (or Power) for about 10 seconds."
        ),
        "VIVO" to mapOf(
            "wifi" to "Settings > WLAN.",
            "bluetooth" to "Settings > Bluetooth.",
            "hotspot" to "Settings > Personal hotspot (or Mobile Network > Personal hotspot).",
            "display" to "Settings > Display & Brightness (Refresh rate, Eye protection, Dark mode, Font size).",
            "sound" to "Settings > Sounds & vibration.",
            "dnd" to "Settings > Sounds & vibration > Do not disturb.",
            "battery" to "Settings > Battery (Power saving, Battery health); Settings > Battery > Background power consumption management > Zyro > Allow high background power consumption.",
            "storage" to "Settings > Storage; iManager > Space cleaner.",
            "apps" to "Settings > Apps & permissions > App manager > the app > Permissions.",
            "notifications" to "Settings > Notifications & status bar > App notifications.",
            "security" to "Settings > Fingerprints / Face and password (security) and Privacy.",
            "location" to "Settings > Location (or Privacy > Location).",
            "update" to "Settings > System update.",
            "developer" to "Settings > About phone > Software version > tap the software version number 7 times; then Settings > System management > Developer options.",
            "accessibility" to "Settings > Accessibility > Downloaded services > Zyro.",
            "keepalive" to "Settings > Apps & permissions > Autostart > Zyro ON; Settings > Battery > Background power consumption management > Zyro > Allow high background power consumption; lock Zyro in Recents; iManager > App manager > Autostart. Accessibility greyed out for a sideloaded app: Settings > Apps > App manager > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Power + Volume Down, or three-finger swipe down; screen recording from the shortcut panel (swipe up from the bottom or the Quick Settings).",
            "restart" to "Hold Power > Restart; frozen phone: hold Power + Volume Up about 10 seconds."
        ),
        "MOTO" to mapOf(
            "keepalive" to "Settings > Apps > Zyro > App battery usage > Unrestricted; Settings > Accessibility > Zyro ON; Display over other apps ON; Microphone, Contacts, Phone, SMS permissions as needed. Sideloaded app and Accessibility greyed out (Android 13+): Settings > Apps > Zyro > three-dot menu > Allow restricted settings.",
            "screenshot" to "Power + Volume Down, or the three-finger touch screenshot gesture (Moto app > Gestures). Moto gestures: chop twice = flashlight, twist wrist = camera."
        ),
        "NOTHING" to mapOf(
            "keepalive" to "Settings > Apps > Zyro > App battery usage > Unrestricted; Settings > Accessibility > Zyro ON; Display over other apps ON; Microphone, Contacts, Phone, SMS permissions as needed. Sideloaded app and Accessibility greyed out (Android 13+): Settings > Apps > Zyro > three-dot menu > Allow restricted settings.",
            "display" to "Settings > Display (Brightness, Dark theme, Adaptive refresh rate, Font size) and Settings > Glyph Interface for the back lights; Essential Space has its own key."
        ),
        "TRANSSION" to mapOf(
            "wifi" to "Settings > Wi-Fi & network (or WLAN).",
            "battery" to "Settings > Battery (Power Saving); Phone Master / Phone Manager > Battery.",
            "storage" to "Phone Master / Phone Manager > Clean; Settings > Storage.",
            "update" to "Settings > System update (or About phone > Software update).",
            "developer" to "Settings > About phone > tap Build number 7 times; then Settings > System > Developer options (or Additional settings).",
            "keepalive" to "Settings > App management > Zyro > Autostart ON, Battery > No restriction; Phone Master / Phone Manager > Autostart manager > allow Zyro; lock Zyro in Recents. Accessibility greyed out for a sideloaded app: App info > three-dot menu > Allow restricted settings."
        ),
        "HUAWEI" to mapOf(
            "update" to "Settings > System & updates > Software update.",
            "developer" to "Settings > About phone > tap Build number 7 times; then Settings > System & updates > Developer options.",
            "battery" to "Settings > Battery (Power saving, Ultra power saving); Phone Manager > Battery.",
            "keepalive" to "Settings > Battery > App launch > Zyro > Manage manually: turn ON Auto-launch, Secondary launch and Run in background; lock the app in Recents. NOTE: newer Huawei phones have no Google services and may not run Android-only apps like Zyro well."
        )

    )

    private val TOPICS: List<Pair<String, List<String>>> = listOf(
        "wifi" to listOf("wifi", "wi-fi", "wlan", "वाईफाई", "वाई-फाई", "internet"),
        "bluetooth" to listOf("bluetooth", "ब्लूटूथ", "pairing", "earbuds"),
        "hotspot" to listOf("hotspot", "tethering", "हॉटस्पॉट"),
        "display" to listOf("display", "brightness", "dark mode", "dark theme", "refresh rate", "screen timeout", "डिस्प्ले", "ब्राइटनेस", "font size"),
        "sound" to listOf("sound", "volume", "ringtone", "vibration", "silent", "साउंड", "वॉल्यूम"),
        "dnd" to listOf("do not disturb", "dnd", "डू नॉट डिस्टर्ब", "focus mode", "modes"),
        "battery" to listOf("battery", "बैटरी", "charging", "power saving", "battery saver"),
        "storage" to listOf("storage", "स्टोरेज", "cleaner", "memory full", "free up", "clean"),
        "apps" to listOf("app info", "uninstall", "default app", "permission", "permissions", "app permission"),
        "notifications" to listOf("notification", "notifications", "नोटिफिकेशन"),
        "security" to listOf("security", "fingerprint", "face unlock", "screen lock", "find my", "lost phone", "play protect", "pin"),
        "location" to listOf("location", "gps", "लोकेशन"),
        "update" to listOf("update", "अपडेट", "software update", "system update"),
        "developer" to listOf("developer", "usb debugging", "build number", "adb"),
        "accessibility" to listOf("accessibility", "talkback", "एक्सेसिबिलिटी"),
        "keepalive" to listOf("zyro", "listening", "wake word", "autostart", "auto start", "background", "keep alive", "kill", "killed", "stops working", "stop working", "restricted settings", "allow restricted", "not responding", "setup", "permissions for zyro"),
        "screenshot" to listOf("screenshot", "screen record", "screen recording", "स्क्रीनशॉट"),
        "restart" to listOf("restart", "reboot", "force restart", "power off", "safe mode", "factory reset", "frozen", "hang", "रीस्टार्ट")

    )

    private fun path(family: String, topic: String): String =
        PATHS[family]?.get(topic) ?: PATHS["STOCK"]?.get(topic) ?: ""

    /** This phone's identity, the Android version notes, and the matching menu paths for this brand (up to 3 topics). */
    fun forTask(t: String): String {
        val i = get()
        val sb = StringBuilder(describe()).append('\n')
        val vn = versionNote(i.sdk)
        if (vn.isNotEmpty()) sb.append(vn).append('\n')
        var n = 0
        for ((topic, keys) in TOPICS) {
            if (n >= 3) break
            if (keys.any { DeviceSkills.hit(t, it) }) {
                val p = path(i.family, topic)
                if (p.isNotEmpty()) {
                    sb.append(topic.uppercase()).append(" on this phone: ").append(p).append('\n')
                    n++
                }
            }
        }
        sb.append("If a menu is not where described, open Settings and use the SEARCH box at the top: it works on every brand.")
        return sb.toString()
    }
}
