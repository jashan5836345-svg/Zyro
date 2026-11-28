package com.zyro.app

/**
 * Everything Zyro should know about using Instagram: profiles, search, follow, likes, comments, saves,
 * sharing, DMs, stories, reels, posts, live, notes, privacy, account and fixes. Like Know.kt, only the
 * best-matching entries are returned so the prompt stays small. Labels are for the normal Android app and
 * can differ a little: the agent is told to trust the real screen first.
 */
object InstagramSkills {

    private const val RULES = "INSTAGRAM RULES: Labels differ by app version, language and account type: read the real labels on screen first and trust them over this text. NEVER post, share a reel/story, send a DM, comment, follow/unfollow, block, delete, archive, change privacy or password, or log out unless the user clearly asked and gave the exact person/text. Never follow, like or comment in bulk: Instagram blocks that. If a popup says 'Try again later', 'Action blocked' or asks to verify, STOP and tell the user. Never type a password, OTP or login code that the user did not give. Do not open third-party downloader or 'who viewed me' sites or apps. If a button is missing (Download, Remix, Message), the creator or Instagram disabled it: say so honestly. Navigate by tapping the real screen."

    private const val LINKS = "INSTAGRAM LINKS (for your knowledge only: you have no tool that opens links, so reach these pages by tapping): a profile = instagram://user?username=NAME or https://www.instagram.com/NAME/ ; a hashtag page = https://www.instagram.com/explore/tags/TAG/ ; Reels = https://www.instagram.com/reels/ ; messages = https://www.instagram.com/direct/inbox/ ; a post or reel link (https://www.instagram.com/p/CODE/ or /reel/CODE/) opens that exact post. Search by tapping the magnifier tab is the fallback."

    private const val PRO_MAP = "INSTAGRAM PROFESSIONAL ACCOUNT TOOLS: Profile > 'Professional dashboard' (or 'Your dashboard') shows reach, accounts engaged, followers and top content. A post or reel > 'View insights' shows plays, likes, comments, shares, saves and reach. Ads/Boost are PAID: never tap Boost or pay. Switch to Professional: Settings and activity > Account type and tools."

    private val FLOWS: List<Pair<List<String>, String>> = listOf(
        listOf("open profile", "profile kholo", "प्रोफाइल खोलो", "username", "@", "account kholo", "dekho", "देखो", "find", "ढूंढो") to
            "IG OPEN A PROFILE: Search tab (magnifier) > the search box on top > type the name > tap the account row (blue tick = verified, shows followers) > the profile opens with the photo, posts/followers/following counts, bio and the Follow / Message buttons. Tabs under the bio: Posts grid, Reels, Tagged. Tap a thumbnail to open it, swipe left/right for photo carousels.",
        listOf("search", "सर्च", "hashtag", "हैशटैग", "explore", "एक्सप्लोर", "audio", "music", "place", "location", "reels search") to
            "IG SEARCH/EXPLORE: bottom bar magnifier = Explore (a grid of reels/photos). Tap the search box on top, type, press the keyboard Search key. Result tabs: For you, Accounts, Audio, Tags, Places (swipe the tabs sideways). A hashtag page shows Top and Recent. Tap an audio result to see all reels using that sound. Recent searches show under the empty box; to remove one tap the X next to it.",
        listOf("follow", "फॉलो", "unfollow", "अनफॉलो", "following", "requested", "private account", "follow request", "remove follower", "followers", "फॉलोअर्स") to
            "IG FOLLOW/UNFOLLOW: on the profile tap the blue 'Follow' (becomes 'Following'; on a private account it becomes 'Requested' until they accept). Unfollow: tap 'Following' > 'Unfollow' (confirm only if the user asked). Your lists: Profile > tap the followers or following number > tabs Followers / Following (each row has Remove or Following buttons; search box on top). Remove a follower: Followers > the 3 dots or 'Remove' > confirm. Follow requests you received: heart/Notifications > 'Follow requests' > Confirm / Delete. Never follow or unfollow many accounts quickly.",
        listOf("like", "लाइक", "heart", "unlike", "double tap", "liked", "पसंद") to
            "IG LIKE: tap the heart under a post / on the right edge of a reel (it turns red; the count changes). Double-tap on the photo/video also likes. Tapping once more removes the like. Your likes: Profile > menu > Your activity > Likes.",
        listOf("comment", "कमेंट", "reply", "रिप्लाई", "delete comment", "pin comment", "turn off comments", "emoji") to
            "IG COMMENTS: tap the speech bubble. The panel lists comments (sort/filter chip at the top on some versions) and the box 'Add a comment...' / 'Join the conversation...' sits at the bottom; type, tap 'Post' or the blue arrow. Reply: 'Reply' under a comment. Like a comment: the small heart at its right. Your own comment: press and hold it (or swipe left) > trash/Delete. Pin (on your own post): hold a comment > pin icon. Turn off comments on your own post: its menu (3 dots) > Turn off commenting. Post only the exact words the user gave.",
        listOf("save", "सेव", "bookmark", "collection", "saved", "कलेक्शन", "unsave") to
            "IG SAVE/COLLECTIONS: tap the bookmark (far right under a post, right edge on a reel); a 'Saved' bar appears at the bottom with 'Add to collection' (tap to choose or create a collection with a name). All saved items: Profile > menu (3 lines) > Saved; chips All, Collections, Posts, Reels, Audio. Remove: tap the filled bookmark again. Collections are private.",
        listOf("share", "शेयर", "send reel", "send post", "भेजो", "bhejo", "forward", "copy link", "लिंक", "add to story", "repost", "रीपोस्ट") to
            "IG SHARE: tap the paper-plane under a post/reel. The sheet has a Search box, a grid of people (tap a name to select, you can choose several), a 'Write a message...' box and the blue 'Send' button; the bottom row has Add to story, WhatsApp, Share to..., Copy link, Download (only some reels), SMS, Threads (scroll it sideways). Repost: the two-arrows icon. Send only to the person the user named. 'Copy link' then paste into another app.",
        listOf("dm", "message", "मैसेज", "chat", "inbox", "इनबॉक्स", "send message", "unsend", "vanish", "voice message", "reply message", "dm bhejo", "text", "bhej") to
            "IG DIRECT MESSAGES: paper-plane icon (top right of Home, or the Messages tab) opens the inbox: chats list, Requests, search box on top ('Search' or 'Ask Meta AI'), compose icon (pencil/plus) = new message. In a chat: back arrow, name (tap = profile and settings), call and video icons on top; the box 'Message...' at the bottom with a mic (HOLD to record, release to send), gallery, sticker and '+', and the blue 'Send' after you type. Hold a message > React / Reply / Copy / Forward / Unsend (Unsend removes it for everyone: only when asked). Chat settings (tap the name): Mute messages, Vanish mode, Nickname, Block/Report, Delete chat (only when asked). Message Requests (from non-followers) are under 'Requests'. Send only the exact text the user gave to the person they named.",
        listOf("story", "स्टोरी", "stories", "highlight", "हाइलाइट", "close friends", "archive", "your story", "story dalo", "post story", "status") to
            "IG STORIES: Watch: tap a coloured ring (top row of Home or a profile photo); tap right = next, tap left = back, hold = pause, swipe down = exit, swipe up on your own = viewers. Reply: the 'Send message' box at the bottom; heart = like. Create: '+' (top left of Home / bottom bar +) > Story > pick a photo/video from the gallery or take one > edit (Aa text, stickers, draw, music) > 'Your story' or 'Close friends' > Share/Send. Highlights are the circles under the bio (Profile > New). Archive: Profile > menu > Archive. NEVER share or post a story unless the user clearly asked and chose the photo.",
        listOf("reel", "reels", "रील", "रील्स", "video", "reel dekho", "watch reel", "next reel", "scroll", "mute", "audio off") to
            "IG REELS VIEWING: bottom bar Reels (clapper icon) or https://www.instagram.com/reels/. Swipe UP = next reel, DOWN = previous. Tap once = pause/play (the speaker icon at the top corner mutes), hold = pause, double-tap = like. Right edge from top: Like (count), Comment, Repost, Share, Save, 3 dots (More: Save, Remix, QR code, Not interested, Report; 'Playback' for quality/captions). Bottom left: username with Follow, caption (tap 'more'), the audio name (tap it to see more reels with that sound). Top: 'Reels / Friends' switch.",
        listOf("create reel", "reel banao", "reel upload", "new reel", "record reel", "reel post", "reels create") to
            "IG CREATE REEL: '+' in the bottom bar (or top left of Home) > 'Reel' > pick a clip from the gallery (tick, Next) or hold the round button to record; 'Add audio' on top picks music; then edit (trim, text, stickers, effects) > Next > write the caption, add a cover, tags, location, audience > 'Share'. Drafts: 'Save draft'. NEVER press Share until the user clearly said which video and caption.",
        listOf("post", "पोस्ट", "photo post", "upload", "अपलोड", "new post", "post banao", "caption", "carousel", "tag", "location", "filters") to
            "IG CREATE POST: '+' > Post > choose one photo (or the multi-select icon for a carousel, up to 20) > Next > filters/edit > Next > write the caption ('Write a caption...'), 'Tag people', 'Add location', audience, then 'Share' (top right). Never press Share unless the user clearly said which photos and what caption. Edit later: your post > 3 dots > Edit; Delete is there too (red, only when asked); Archive hides it without deleting.",
        listOf("live", "लाइव", "go live", "stream") to
            "IG LIVE: '+' > Live (swipe the mode at the bottom) > optional title/audience > 'Go Live'; the viewers and comments show on screen; end with 'End' (top) > End now. Only start when the user clearly asked.",
        listOf("notes", "नोट", "note", "note dalo", "status note") to
            "IG NOTES: in the DM inbox, the bubble above your profile photo ('Share a note') lets you write a short note (60 chars) visible to followers for 24 h: type it, choose the audience (Followers you follow back / Close friends), then 'Share'. Only when asked.",
        listOf("notification", "नोटिफिकेशन", "activity", "bell", "heart icon", "mention", "mentions", "tagged", "टैग") to
            "IG NOTIFICATIONS: heart icon at the top right of Home opens activity: Follow requests, New (today), This week, Earlier; tap an item to open it. Tagged posts: Profile > the tag icon tab. Mentions: Settings and activity > Tags and mentions. Notification switches: Settings and activity > Notifications.",
        listOf("privacy", "प्राइवेसी", "private", "public", "close friends list", "block", "ब्लॉक", "restrict", "mute", "म्यूट", "hidden words", "limit", "comments control", "who can message") to
            "IG PRIVACY: Profile > menu (3 lines) > Settings and activity > 'Account privacy' (round toggle Private account). 'Close Friends' (create/edit the list), 'Blocked', 'Messages and story replies', 'Tags and mentions', 'Comments', 'Hidden Words', 'Restricted accounts', 'Muted accounts', 'Limit interactions'. Block someone: their profile > 3 dots (top right) > Block (confirm). Restrict/Mute: profile > Following > Mute / Restrict. Change only what the user asked, then check the toggle changed.",
        listOf("account", "अकाउंट", "switch account", "add account", "password", "पासवर्ड", "log out", "logout", "लॉगआउट", "sign out", "username change", "delete account", "deactivate", "data", "download my data", "time", "limit", "daily limit", "screen time") to
            "IG ACCOUNT: Profile > menu > 'Accounts Center' (password, security, personal details), 'Account type and tools', 'Your activity' (Time spent, Links you've visited), 'Time management' (Daily limit, Sleep mode), 'Archiving and downloading', 'Download your information' (in Accounts Center). Switch account: tap your username at the top of the Profile > pick an account or 'Add Instagram account'. Log out (at the very bottom of Settings) only when asked. Never change the password, delete or deactivate unless the user clearly asked; never type a password or login code you were not given.",
        listOf("edit profile", "bio", "बायो", "profile photo", "change name", "change photo", "link in bio", "profile song", "pronouns", "banner", "qr") to
            "IG EDIT PROFILE: Profile > 'Edit profile': Name, Username, Pronouns, Bio, Add link, Banners, Gender, Music; then the tick/'Done' at the top right. Change photo: tap 'Edit picture or avatar' > New profile picture > choose > Done. QR: Profile > 'Share profile' > QR code (Download / Copy link). Only change what the user asked and say what changed.",
        listOf("not interested", "interested", "feed", "फीड", "suggested", "recommend", "ads", "विज्ञापन", "sponsored", "following feed", "favorites", "favourites", "reset suggestions") to
            "IG FEED CONTROL: Home top-left Instagram logo opens a menu: Following (only followed accounts, newest first), Favorites (a short list you choose), (plus the default For you). On a post: 3 dots > 'Not interested' / 'Snooze suggested posts' / 'Mute' / 'Report'. Sponsored posts say 'Sponsored': ignore them, never tap their buttons. Settings and activity > Content preferences > Reset suggested content.",
        listOf("download", "डाउनलोड", "save video", "reel download", "download reel", "save photo", "download story") to
            "IG DOWNLOAD: a public reel: tap the paper-plane, scroll the bottom row of the share sheet sideways to 'Download' and tap it (saved to Gallery/Photos). If the row has no Download the creator turned it off: tell the user honestly and stop. Your OWN stories/posts: open it > 3 dots > Save / Save to camera roll. Settings and activity > Archiving and downloading has 'Save original photos'. Use no downloader websites or apps.",
        listOf("threads", "थ्रेड्स", "broadcast", "channel", "चैनल", "collab", "collab post", "close") to
            "IG THREADS/CHANNELS: the 'Threads' button on the Profile opens the separate Threads app (install from Play Store only if asked). Broadcast channels appear in the DM inbox with a megaphone icon; tap one to read, join/leave with the top button. Collab posts show two usernames on top.",
        listOf("problem", "दिक्कत", "not loading", "नहीं चल", "error", "एरर", "crash", "action blocked", "try again later", "stuck", "slow", "feed not refreshing", "can't", "cant", "login problem", "logged out", "verify", "suspicious") to
            "IG FIXES: feed/reels not loading: swipe down on the feed to refresh, check Wi-Fi/data, close and reopen the app. Still stuck: Android Settings > Apps > Instagram > Storage > Clear cache (not Clear data without asking). 'Try again later' / 'Action blocked': stop all likes/follows/comments for 24 h, do not retry. Login or verification wall: the user must type the code or password; use ask_user. Outdated app: Play Store > Instagram > Update."
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

    /** Rules first (so they survive any cut-off), then the best 3 matching procedures and the links. */
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
        if (t.contains("insight") || t.contains("dashboard") || t.contains("boost") || t.contains("analytics") || t.contains("reach")) sb.append(PRO_MAP).append('\n')
        return sb.toString().take(3600)
    }
}
