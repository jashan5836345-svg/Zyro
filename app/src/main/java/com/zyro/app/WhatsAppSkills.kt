package com.zyro.app

/**
 * Everything Zyro should know about using WhatsApp: chats, messages, media, voice notes, calls, groups, status, channels,
 * communities, message actions, search, organising chats, disappearing messages, privacy, account, linked devices,
 * backup, notifications, storage, appearance, stickers and AI, broadcasts, payments, WhatsApp Business and fixes.
 * Like Know.kt and InstagramSkills.kt, only the best-matching entries are returned so the prompt stays small.
 * Labels are for the normal Android app and can differ a little: the agent is told to trust the real screen first.
 */
object WhatsAppSkills {

    private const val RULES = "WHATSAPP RULES: Labels differ by app version, language and phone: read the real labels on screen first and trust them over this text. NEVER send a message, make a call, start a payment, post a status, join, leave or delete a group or chat, block someone or change a privacy setting unless the user clearly asked for exactly that. Before sending, check in the message box that the text and the chat name are the ones the user asked for. If a contact or chat is not found, say so instead of picking a similar name. Never read out, copy or type one-time codes (OTP), the 6-digit verification code, the two-step PIN or a UPI PIN. Ticks: one grey = sent, two grey = delivered, two blue = read. Stop at 'Account banned' or 'Try again later' screens and tell the user."

    private const val LINKS = "WHATSAPP LINKS (for your knowledge only: you have no tool that opens links, so reach these pages by tapping): a chat with a number = whatsapp://send?phone=91XXXXXXXXXX&text=HELLO or https://wa.me/91XXXXXXXXXX?text=HELLO (country code first, no plus sign or spaces; the text is only filled in, someone still has to tap Send). A group invite = chat.whatsapp.com/CODE. A channel = whatsapp.com/channel/CODE."

    private const val BUSINESS = "WA BUSINESS APP: Settings > Business tools: Profile (hours, address, website), Catalog (Add new product), Messaging tools (Greeting message, Away message, Quick replies: type / in the chat box to pick one), Labels (coloured tags on chats), Advertise, Short link. The attach menu of a chat also has Catalog and Payment. Messages to customers follow the same sending rules as above."

    private val FLOWS: List<Pair<List<String>, String>> = listOf(
        listOf("open chat", "chat kholo", "चैट खोलो", "find chat", "contact", "ढूंढो", "dhundo", "search chat", "kaun sa chat") to
            "WA OPEN A CHAT: Chats tab > search (magnifier, or the 'Ask Meta AI or Search' box on top) > type the name > tap the matching row under Chats (people without a chat are under 'Contacts on WhatsApp', unknown numbers under 'Not in your contacts'). If two contacts have the same name, read the number or last message before tapping, or ask the user. Search inside one chat: open it > ⋮ > Search.",
        listOf("send message", "message bhejo", "मैसेज भेजो", "msg", "likh", "लिख", "type", "reply", "जवाब", "text") to
            "WA SEND TEXT: open the chat > tap the box 'Message' > type > the green Send arrow on the right (a mic icon there means the box is still empty). Look at the text once before sending. Reply to one message: swipe it to the right (or long-press > Reply arrow), then type and send. Emoji, GIF and sticker: the smiley at the left of the box.",
        listOf("photo", "फोटो", "video", "वीडियो", "document", "file", "फाइल", "attach", "location", "लोकेशन", "gallery", "camera", "pdf", "contact share", "view once") to
            "WA SEND PHOTO/VIDEO/FILE: in the chat tap the paperclip (or the + on some versions) next to the box > Gallery, Camera, Document, Audio, Location, Contact, Poll or Event. Gallery: tap the pictures to select them (a number appears), optional caption at the bottom, then the green Send arrow; 'HD' at the top of the preview sends higher quality; the circled '1' makes it view once. Document: pick the file, confirm Send. Location: 'Send your current location' (live location is a separate choice: ask first).",
        listOf("voice note", "वॉइस", "audio message", "record", "रिकॉर्ड", "mic", "बोलकर भेजो", "transcribe") to
            "WA VOICE NOTE: hold the mic at the right of the box while speaking, release to send, slide left to cancel, slide up to lock hands-free recording (then tap the green Send, or the bin to throw it away). On a received note tap '1x' for 1.5x or 2x speed. Long-press a note > Transcribe shows it as text if Settings > Chats > Voice message transcripts is on.",
        listOf("call", "कॉल", "video call", "voice call", "ring", "missed", "मिस्ड", "phone kar", "फोन कर") to
            "WA CALLS: in a chat the phone icon (voice) and the camera icon (video) are in the top bar. Calls tab: the green new-call button, pick the person; the list shows missed (red), incoming and outgoing calls, tap the phone icon on a row to call back. In a call: mute, speaker, video, end (red). Group call: open the group > call icon > choose people. NEVER start a call unless the user asked; say out loud who you are calling.",
        listOf("group", "ग्रुप", "create group", "new group", "add member", "remove member", "admin", "exit group", "leave group", "group info", "group name", "description", "invite link") to
            "WA GROUPS: create: Chats tab > ⋮ (or the green new-chat button) > New group > tick people > green arrow > group name (and photo) > green tick. Group info: open the group and tap its name on top: edit name, photo, description; Add members; long-press a member > Make group admin / Remove / Message; Group settings (who can edit info, send messages, add people, approve new members); Invite via link; Disappearing messages; at the bottom Exit group and Report. Only admins can remove people. NEVER leave, delete or remove anyone unless the user clearly asked.",
        listOf("status", "स्टेटस", "updates", "story", "स्टोरी", "post status", "view status", "status privacy", "mute status", "status lagao") to
            "WA STATUS/UPDATES: Updates tab (older versions: Status). 'Status' row: My status (the + or camera to add: camera, gallery, text pencil, voice) and below it 'Recent updates' with contacts' statuses: tap to watch, tap right for the next, left for the previous. On your own status swipe up to see who viewed it. Status privacy: Updates > ⋮ > Status privacy (My contacts / My contacts except / Only share with). Mute someone's status: long-press it > Mute. Delete yours: open it > the bin or ⋮ > Delete. NEVER post a status unless asked.",
        listOf("channel", "चैनल", "community", "कम्युनिटी", "follow channel", "announcement") to
            "WA CHANNELS/COMMUNITIES: Updates tab > Channels list (Find channels, open one > Follow; the bell icon mutes or unmutes). A Community has its own tab (or sits in the chat menu on some versions): it holds several groups, 'Announcements' is only written by admins, tap a group name inside to open it.",
        listOf("delete message", "edit message", "forward", "फॉरवर्ड", "star", "react", "reaction", "pin message", "copy", "message info", "delete for everyone", "unsend", "स्टार", "डिलीट", "एडिट") to
            "WA MESSAGE ACTIONS: long-press a message (tap more messages to select several), then use the top bar: Reply, Copy, Forward (arrow, then choose chats and Send), Star, Delete (bin: 'Delete for me', or 'Delete for everyone' for your own message within about two days), ⋮ for Info (read and delivered times), Pin, Edit (own text message within 15 minutes, then the tick), Report. React: long-press and tap an emoji (+ for more). Starred messages: Chats ⋮ > Starred. NEVER delete for everyone without a clear instruction.",
        listOf("search message", "media", "photos in chat", "links", "docs", "सर्च", "ढूंढ", "old message", "purana message", "unread") to
            "WA SEARCH: the search box on the Chats tab finds chats, contacts and messages; the chips below it (Photos, Videos, Links, GIFs, Audio, Documents, Polls, Unread, Groups) narrow the results. Inside a chat: ⋮ > Search with up and down arrows. A chat's pictures and files: tap the chat name > Media, links and docs.",
        listOf("archive", "pin", "mute", "unmute", "म्यूट", "mark unread", "favourites", "favorites", "list", "delete chat", "clear chat", "export chat", "chat history", "label") to
            "WA ORGANISE CHATS: long-press a chat in the list: pin (up to 3), mute (choose 8 hours, 1 week or Always), archive (box with an arrow; archived chats are under 'Archived'), then ⋮ for Mark as unread, Delete chat, Add to list. The chips under the search box (All, Unread, Favourites, Groups) are chat lists. Clear or export one chat: open it > ⋮ > More > Clear chat / Export chat (with or without media). NEVER delete or clear a chat unless asked.",
        listOf("disappearing", "view once", "auto delete", "gayab", "timer", "24 hours", "90 days") to
            "WA DISAPPEARING MESSAGES: open the chat > tap its name > Disappearing messages > 24 hours / 7 days / 90 days / Off (affects only new messages). Default for all new chats: Settings > Privacy > Default message timer. 'View once' is the circled 1 on the send button of a photo or video.",
        listOf("privacy", "प्राइवेसी", "last seen", "online", "profile photo", "about", "read receipts", "blue tick", "ब्लू टिक", "who can", "block", "ब्लॉक", "unblock", "blocked", "app lock", "chat lock", "lock chat", "screen lock", "fingerprint", "लॉक") to
            "WA PRIVACY: Chats ⋮ > Settings > Privacy: Last seen and online, Profile photo, About, Links, Status, Read receipts (off hides the blue ticks), Default message timer, Groups (who can add you), Calls (silence unknown callers), Blocked contacts (tap a name to unblock; Add to block), Advanced, Privacy checkup (guided review), App lock (fingerprint or face to open WhatsApp). Chat lock: open a chat > tap its name > Chat lock (or long-press the chat); locked chats move to a Locked chats folder. Block one person: open the chat > ⋮ > More > Block. NEVER block, unblock or change a setting unless the user asked for that exact one.",
        listOf("account", "अकाउंट", "change number", "delete account", "two-step", "two step", "passkey", "email", "security", "verification", "सिक्योरिटी", "number badlo") to
            "WA ACCOUNT: Settings > Account: Passkeys, Email address, Two-step verification (a 6-digit PIN: never say it or type it for anyone else), Change number, Request account info, Delete account (permanent: ask the user twice). 'Linked devices' is a separate row in Settings. Never read out or enter codes you see in messages.",
        listOf("web", "whatsapp web", "linked device", "qr", "laptop", "कंप्यूटर", "computer", "scan", "companion", "लैपटॉप") to
            "WA LINKED DEVICES/WHATSAPP WEB: Settings (or ⋮ on Chats) > Linked devices > Link a device (unlock if asked) > point the camera at the QR code on web.whatsapp.com, or choose 'Link with phone number instead' and type the 8-letter code shown on the computer. The list shows each device: tap one > Log out. Only link a computer the user is looking at.",
        listOf("backup", "बैकअप", "google drive", "restore", "chat backup", "new phone", "naya phone", "encrypted backup") to
            "WA BACKUP: Settings > Chats > Chat backup: Back up now (green button), Back up to Google Drive (Never / Daily / Weekly / Monthly / Only when I tap), Google Account (which one), Include videos, End-to-end encrypted backup (a password or a 64-digit key: tell the user to keep it safe). Restore: on a new phone install WhatsApp, verify the same number and tap Restore when it asks.",
        listOf("notification", "नोटिफिकेशन", "ringtone", "tone", "sound", "आवाज़", "vibration", "popup", "message tone", "call ringtone", "late", "देर से") to
            "WA NOTIFICATIONS: Settings > Notifications: Conversation tones, Reminders, and the blocks Messages / Groups / Calls, each with Notification tone, Vibrate, Popup or Light and High priority notifications; Reaction notifications has its own switch. One chat only: tap its name > Notifications (custom tone) or Mute notifications. Messages arriving late: Android Settings > Apps > WhatsApp > Battery > Unrestricted, and allow background data.",
        listOf("storage", "स्टोरेज", "data", "डाटा", "auto download", "auto-download", "low data", "space", "manage storage", "delete media", "photos not saving", "gallery me") to
            "WA STORAGE AND DATA: Settings > Storage and data > Manage storage (chats sorted by size, filters 'Larger than 5 MB' and 'Forwarded many times', select and Delete). Media auto-download: When using mobile data / When connected on Wi-Fi / When roaming: tick Photos, Audio, Video, Documents. Media upload quality: Standard or HD. Use less data for calls. Per chat 'Media visibility' (tap the name > Media visibility) hides its photos from the Gallery.",
        listOf("wallpaper", "वॉलपेपर", "theme", "dark mode", "डार्क", "font", "language", "भाषा", "chat color", "background") to
            "WA APPEARANCE: Settings > Chats: Theme (System default / Light / Dark), Default chat theme (colour and wallpaper), Font size, Enter is send, App language. One chat's wallpaper: open the chat > ⋮ > Wallpaper.",
        listOf("sticker", "स्टीकर", "gif", "avatar", "ai", "meta ai", "imagine", "emoji", "इमोजी") to
            "WA STICKERS/GIF/AI: the smiley next to the message box opens Emoji, GIF, Sticker and Avatar tabs with a search box. Own stickers: Sticker tab > + / Create. Meta AI: the blue-ringed circle in Chats (or the 'Ask Meta AI' box): ask a question, or type /imagine and a description for a picture. Meta AI is not a contact: never use it to message people.",
        listOf("broadcast", "ब्रॉडकास्ट", "bulk", "many people", "sabko", "सबको", "same message to everyone", "mass") to
            "WA SEND TO MANY: Broadcast list: Chats ⋮ > New broadcast > pick contacts (they must have your number saved) > write and send: replies come to you privately. Or forward one message to up to five chats at once. Do not send to many people unless the user clearly asked and confirmed who.",
        listOf("poll", "पोल", "event", "payment", "पेमेंट", "upi", "money", "पैसे", "rupee", "pay", "bill", "send money") to
            "WA POLLS/EVENTS/PAYMENTS: Poll: paperclip > Poll > question and options > send. Event (groups): paperclip > Event. Payments (India): the rupee icon or paperclip > Payment needs a bank account and a UPI PIN. NEVER start, confirm or enter a PIN for a payment: stop and ask the user to do that step themselves.",
        listOf("profile", "प्रोफाइल", "my name", "about text", "edit profile", "qr code", "username", "apna naam", "photo badlo") to
            "WA YOUR PROFILE: tap your photo at the top (or Settings > your name): change Name, About (pencil), Links, and the camera on the photo to change it; the QR icon shows or scans a WhatsApp QR code. A username (where available) is in the same profile list.",
        listOf("new contact", "add contact", "save number", "नंबर सेव", "invite", "unknown number", "contact info", "naya contact") to
            "WA CONTACTS: new chat button (green) > New contact (name and number > Save; the number must be on WhatsApp). Tap a chat's name for Contact info: media, starred, notifications, disappearing messages, encryption, chat lock, block, report. A chat with a number that is not saved can also be opened with the link in the LINKS note.",
        listOf("problem", "दिक्कत", "not sending", "clock", "waiting for this message", "connecting", "stuck", "one tick", "not working", "error", "can't", "cant", "verification code", "code nahi", "logged out", "slow", "crash", "नहीं चल", "नहीं जा रहा") to
            "WA FIXES: one grey tick or a clock = no internet on your side or theirs: check data and Wi-Fi, switch flight mode on and off. 'Waiting for this message. This may take a while' = the sender must open WhatsApp: wait. Stuck on Connecting: Android Settings > Apps > WhatsApp > Storage > Clear cache (NOT Clear data, which removes local chats), allow background data, Battery Unrestricted; the phone date and time must be automatic; update WhatsApp in the Play Store. No verification code: wait 60 seconds, choose 'Call me', check the number and the signal. Never share the six-digit code."
    )

    /** True when the key is in the text as a whole word (or word start for long words); Hindi keys match anywhere. */
    private fun hit(t: String, k: String): Boolean {
        if (k.any { it.code > 127 }) return t.contains(k)
        var i = t.indexOf(k)
        while (i >= 0) {
            val before = i == 0 || !t[i - 1].isLetterOrDigit()
            val end = i + k.length
            val after = end >= t.length || !t[end].isLetterOrDigit()
            if (before && (after || k.length >= 5)) return true
            i = t.indexOf(k, i + 1)
        }
        return false
    }

    /** Rules first (so they survive any cut-off), then the best 2 matching procedures and the links. */
    fun forTask(job: String): String {
        val t = job.lowercase()
        val best = FLOWS.map { (keys, text) -> keys.count { hit(t, it) } to text }
            .filter { it.first > 0 }
            .sortedByDescending { it.first }
            .take(2)
        val sb = StringBuilder()
        sb.append(RULES).append('\n')
        for ((_, text) in best) sb.append(text).append('\n')
        sb.append(LINKS).append('\n')
        if (t.contains("business") || t.contains("catalog") || t.contains("quick reply") || t.contains("away message") || t.contains("बिजनेस")) sb.append(BUSINESS).append('\n')
        return sb.toString().take(3600)
    }
}
