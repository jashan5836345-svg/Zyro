package com.zyro.app

/**
 * Everything Zyro should know about using YouTube (viewer, Shorts, playlists, history, downloads, live,
 * uploading) and the YouTube Studio app. Like Know.kt, only the few entries that match the task are
 * returned, so the prompt stays small and fast. Button labels are written from the normal Android app
 * and may differ a little on other versions: the agent is told to trust the real screen first.
 */
object YouTubeSkills {

    private const val RULES = "YOUTUBE RULES: Button labels differ by app version, language and screen size: read the real labels on screen first and trust them over this text. NEVER upload, delete a video/comment/playlist, post a comment, report, unsubscribe, change privacy or buy/start Premium, Super Chat, Super Thanks, memberships, rentals unless the user clearly asked and gave the exact text. YouTube Premium features (background play, 1080p Premium, ad-free, some Picture-in-picture) are paid: tell the user honestly, never pay. If a button is not there (Download on Shorts, Clip, Remix), the creator or YouTube disabled it: say so, do not hunt for hacks or downloader sites. Fast ways first: a search link or deep link beats tapping through menus."

    private const val LINKS = "YOUTUBE DEEP LINKS (use open_link or open_app first, they are faster than tapping): search = https://www.youtube.com/results?search_query=WORDS (spaces as +); one video = vnd.youtube:VIDEOID or https://www.youtube.com/watch?v=VIDEOID; Shorts feed = https://www.youtube.com/shorts; Subscriptions feed = https://www.youtube.com/feed/subscriptions; History = https://www.youtube.com/feed/history; Library/You = https://www.youtube.com/feed/library; Watch later = https://www.youtube.com/playlist?list=WL; Liked videos = https://www.youtube.com/playlist?list=LL; a channel = https://www.youtube.com/@handle; a playlist = https://www.youtube.com/playlist?list=ID. Open them with the YouTube app package so they do not open in a browser."

    private const val STUDIO_MAP = "YOUTUBE STUDIO APP (package com.google.android.apps.youtube.creator) MAP: bottom bar Dashboard, Content, Playlists, Analytics, Comments (labels vary). Top right: + to create, bell. Dashboard: last video performance, channel summary, quick stats. Content: tabs Videos, Shorts, Live, Posts; tap a video then the pencil Edit: Title, Description, Visibility, Thumbnail, Audience, Comments settings, Save. Analytics tabs: Overview, Content, Audience, Revenue (Earn); pick the range with the date chip (7/28/90 days, 365, Lifetime). Key numbers: Views, Watch time, Subscribers, Impressions click-through rate, Average view duration. Comments: tabs Published / Held for review / Spam; on a comment: Reply, heart, pin (⋮), remove (⋮) and approve/hold. Never publish, delete, change visibility or reply unless the user clearly asked."

    private val FLOWS: List<Pair<List<String>, String>> = listOf(
        listOf("play", "chalao", "चलाओ", "चला", "खोलो", "video", "वीडियो", "gaana", "गाना", "song", "watch", "देख") to
            "YT PLAY: easiest is the search link, the first good result is a vertical list: tap the video TITLE (not the Shorts shelf or an ad labelled Sponsored). The player starts by itself; done = the player is on screen and the time is moving. If an ad plays, wait for 'Skip' (up to 5 s) and tap it; unskippable ads must be waited out. For music add the word 'song' or 'official audio' to the query.",
        listOf("pause", "रोको", "रुको", "resume", "aage", "आगे", "पीछे", "seek", "forward", "rewind", "skip", "10 second", "full screen", "fullscreen", "फुल स्क्रीन") to
            "YT PLAYER CONTROLS: tap the video once to show controls (they hide after 3 s): centre = pause/play, left and right arrows = previous/next in the queue, bottom = the time bar (drag to jump), bottom right = full screen (rotate icon / expand arrows), top = minimise (down arrow), cast, CC, gear. DOUBLE-TAP the right side = forward 10 s, left side = back 10 s (more taps add more). Swipe UP on the player = full screen, swipe DOWN = back to normal. HOLD a finger on the video = temporary 2x speed. Swipe down on the player in full screen exits it.",
        listOf("next video", "अगला वीडियो", "queue", "क्यू", "autoplay", "ऑटोप्ले", "play next", "previous video") to
            "YT QUEUE/AUTOPLAY: a video in a list: ⋮ > 'Play next in queue' (plays right after the current one) or 'Add to queue'. On the video page the Autoplay switch sits at the top of the player or in the queue bar at the bottom (round toggle). Tap a suggested video under the player to switch to it. Settings > Playback > Autoplay controls the default.",
        listOf("speed", "स्पीड", "fast", "slow", "तेज", "धीमा", "1.5", "2x", "playback") to
            "YT SPEED: tap the player > gear > 'Playback speed' > choose 0.25 to 2.0 (or the 1.25/1.5/2.0 buttons). Quick way: HOLD on the video for 2x, release to go back. Custom speed: gear > Playback speed > Custom.",
        listOf("caption", "captions", "subtitle", "subtitles", "सबटाइटल", "कैप्शन", "cc", "transcript", "ट्रांसक्रिप्ट", "translate", "ट्रांसलेट") to
            "YT CAPTIONS: tap the player, then the CC button at the top (turns red/filled when on) or gear > Captions > pick a language or 'Auto-translate' (gear > Captions > Auto-translate > language) when the video has captions. Transcript: tap the description area ('...more') and look for 'Show transcript'. Always-on captions: Settings > Captions > turn on. Shorts: ⋯ at the top right > Captions.",
        listOf("quality", "क्वालिटी", "hd", "720", "1080", "480", "360", "data saver") to
            "YT QUALITY: tap the player > gear > Quality > 'Advanced' shows the exact list (1080p, 720p, 480p, 360p, 240p, 144p) or choose Auto / Higher picture quality / Data saver. 1080p Premium is locked. Default per network: Settings > Video quality preferences.",
        listOf("background", "बैकग्राउंड", "screen off", "lock screen", "picture in picture", "pip", "mini", "miniplayer", "मिनी") to
            "YT BACKGROUND/PIP/MINI: the down arrow at the top left of the player shrinks it into a mini player at the bottom (swipe it away or tap X to close, tap it to expand). Picture-in-picture: press Home while a video plays (needs Settings > Playback > Picture-in-picture on, and in some countries Premium). Playing with the screen off is a Premium-only feature: tell the user honestly. 'Lock screen' in the gear menu only locks touches.",
        listOf("cast", "कास्ट", "tv", "टीवी", "watch on tv", "chromecast") to
            "YT CAST/TV: tap the cast icon (a screen with waves) on the player and pick the TV; or Settings > Watch on TV. Zyro also has the tv tool: command 'youtube' with a query shows a YouTube search directly on the paired TV.",
        listOf("share", "शेयर", "copy link", "link", "लिंक", "भेजो") to
            "YT SHARE: tap Share (arrow) in the row under the title or on the right edge of a Short; the sheet shows recent people/apps, a row of apps (WhatsApp, Instagram, Gmail...), 'Copy link' and 'More'. To send on WhatsApp pick it, choose the contact, then press the green Send only if the user asked. Add a start time: Share sheet > 'Start at' (on some versions).",
        listOf("subscribe", "सब्सक्राइब", "unsubscribe", "bell", "बेल", "notification", "नोटिफिकेशन", "channel", "चैनल", "subscribed") to
            "YT CHANNEL + SUBSCRIBE: tap the channel name/photo under the title to open the channel page. Tabs on top (swipe sideways): Home, Videos, Shorts, Live, Playlists, Posts/Community, About; chips Latest / Popular / Oldest sort videos. 'Subscribe' (black) becomes 'Subscribed' (grey). Bell: tap 'Subscribed' > All / Personalised / None / Unsubscribe. Never tap Unsubscribe unless asked. The channel search = magnifier on the channel page.",
        listOf("subscription", "subscriptions", "सब्सक्रिप्शन फीड", "new videos", "नए वीडियो", "feed", "फीड") to
            "YT SUBSCRIPTIONS FEED: bottom bar 'Subscriptions' shows the newest videos of followed channels; the channel circles on top: tap one to see only it; 'All' opens the full channel list (with Sort and Manage). Open it directly with https://www.youtube.com/feed/subscriptions.",
        listOf("like", "लाइक", "dislike", "डिसलाइक", "thumb") to
            "YT LIKE/DISLIKE: thumbs-up / thumbs-down in the row under the title (a Short: on the right edge). One tap = on (filled), tap again = off. The count shows next to the thumb. Liked videos are saved in You > Playlists > Liked videos.",
        listOf("comment", "कमेंट", "reply", "रिप्लाई", "pin", "heart", "delete comment") to
            "YT COMMENTS: tap the grey 'Comments' bar under the title (a Short: the speech bubble). Top of the panel: sort chips Top / Newest. Add: box 'Add a comment...' > type > blue send arrow. Reply: the 'Reply' link under a comment. Like/dislike with the thumbs at the right of a comment. Your own comment: ⋮ > Edit / Delete (Delete needs a second tap). Post only the exact words the user gave. Pin and heart are creator tools (see YouTube Studio).",
        listOf("save", "सेव", "watch later", "बाद में", "playlist", "प्लेलिस्ट", "add to playlist", "new playlist", "नई प्लेलिस्ट", "remove from playlist", "delete playlist") to
            "YT PLAYLISTS: Save: video page ⋯ > Save (or ⋮ in a list > Save to playlist; Short: the Save icon) opens 'Save video to...' with tick boxes (Watch later, your playlists) then Done. NEW playlist: in that sheet tap '+ New playlist' > type a name > Privacy (Private / Unlisted / Public, choose Private unless told) > Create. Remove a video: You > Playlists > the playlist > ⋮ next to the video > 'Remove from playlist'. Rename/delete a playlist: open it > ⋮ (top right) > Edit/Delete playlist (delete only when asked). Play all / Shuffle buttons sit on top of an opened playlist.",
        listOf("history", "हिस्ट्री", "watch history", "clear history", "delete history", "pause history", "incognito", "इन्कॉग्निटो", "private mode") to
            "YT HISTORY/PRIVACY: You tab > History (list, search box, ⋮ on a video > Remove from watch history). Clear or pause: You > ⚙ > 'Manage all history' (or Settings > History and privacy) > 'Pause watch history' / 'Clear watch history' / 'Auto-delete'. Incognito: tap your profile photo (top right of Home) > 'Turn on Incognito' (a grey incognito icon shows; nothing is saved to history). Clear or delete only when the user asked.",
        listOf("download", "डाउनलोड", "downloads", "offline", "ऑफलाइन", "smart downloads", "delete download") to
            "YT DOWNLOAD MANAGEMENT: You tab > Downloads shows the saved videos and their size; play offline from there. Remove one: the ⋮ on the video > 'Delete from downloads'. Settings > Downloads: Download quality (Ask each time / Medium 360p / Low 144p), Download over Wi-Fi only, Smart downloads, Downloads location. 720p/1080p downloads and background play need Premium. A Short, a live stream or a video whose creator disabled it cannot be downloaded: say so.",
        listOf("short", "shorts", "शॉर्ट", "शॉर्ट्स", "reel", "रील") to
            "YT SHORTS: bottom bar 'Shorts' (https://www.youtube.com/shorts). Swipe UP = next, DOWN = previous. Tap once = pause/play, double-tap = like. Right edge top to bottom: Like, Dislike, Comments, Share, Save/Remix; ⋯ top right: Description, Captions, Quality, Not interested, Report; bottom: channel + Subscribe + title + sound name (tap the sound to see other Shorts with it). Search chip 'Shorts' filters search to Shorts only.",
        listOf("live", "लाइव", "stream", "स्ट्रीम", "chat", "super chat", "go live") to
            "YT LIVE: Home chip 'Live' or search filter Features > Live lists live streams. In a stream the chat is under the player (box 'Chat publicly...' + send arrow); only post what the user told you. Super Chat/Super Stickers/Join (memberships) are PAID: never tap them. GO LIVE yourself: + > Live > allow camera and mic > title > Visibility > Next (3-2-1) > Go live; end with the red 'End'. Needs a verified channel and often 50 subscribers on mobile: only start when the user clearly asked.",
        listOf("clip", "क्लिप", "scissors", "trim clip") to
            "YT CLIP: when the channel allows it, a 'Clip' (scissors) button sits in the row under the title (sometimes inside ⋯). Tap it, drag the handles (5-60 s), add a title, tap 'Share clip'. Your clips: You > Your clips. If there is no Clip button the creator turned it off.",
        listOf("not interested", "interested नहीं", "रुचि नहीं", "dont recommend", "don't recommend", "block channel", "recommend", "रिकमेंड", "report", "रिपोर्ट") to
            "YT FEED CONTROL: ⋮ on a video in a list > 'Not interested' (hides this video) or 'Don't recommend channel'. Report: ⋮ or ⋯ > Report > choose the reason > Next/Submit (only when the user asked). Settings > Manage all history can also reset recommendations by removing watch history.",
        listOf("search", "सर्च", "ढूंढ", "filter", "फिल्टर", "voice search", "trending", "hashtag", "हैशटैग", "today", "this week", "upload date", "duration") to
            "YT SEARCH+FILTERS: magnifier at the top > type or tap the mic for voice search > the keyboard Search key. Chips: All / Shorts / Unwatched / Watched / Videos. Filters: ⋮ top right > Search filters: Type (Video, Channel, Playlist, Movie), Duration (under 3 min, 3-20 min, over 20 min), Upload date (Last hour, Today, This week, This month, This year), Features (Live, 4K, HD, Subtitles/CC, Creative Commons, 360, VR180, HDR), Sort by (Relevance, Upload date, View count, Rating), then Apply. A hashtag in the query (#word) opens the tag page. For 'newest' sort by Upload date.",
        listOf("description", "डिस्क्रिप्शन", "chapter", "चैप्टर", "timestamp", "टाइमस्टैंप", "more", "views", "व्यूज") to
            "YT DESCRIPTION/CHAPTERS: tap the title/description area (shows views and date, '...more') to open the full description: links, hashtags, chapters, 'Show transcript'. Chapters also show as a list under the time bar; tap one to jump. Timestamps like 2:35 in the description or comments are tappable and jump to that second.",
        listOf("profile", "प्रोफाइल", "account", "अकाउंट", "switch account", "sign in", "साइन इन", "sign out", "log out", "logout") to
            "YT ACCOUNT: your profile photo (top right of Home) opens the menu: Switch account, Turn on Incognito, Your channel, Time watched, Get Premium (never), Settings, Help. Switch account lists the signed-in Google accounts: tap one. Sign out only when the user clearly asked.",
        listOf("movie", "फिल्म", "movies", "rent", "किराए", "buy", "purchase", "podcast", "पॉडकास्ट", "premium", "प्रीमियम", "membership", "free movies") to
            "YT MOVIES/PODCASTS/PREMIUM: You > Movies & TV shows your rentals and free movies with ads; a movie with a price (Rent/Buy) is PAID: never press it. Podcasts: search the name, filter Playlist, or the Podcasts shelf on Home. Premium/Music upsell screens: close with X or 'No thanks', never start a trial.",
        listOf("upload", "अपलोड", "create", "post", "पोस्ट", "video upload", "new video", "publish", "thumbnail", "थंबनेल", "schedule", "visibility", "public", "private", "unlisted", "title", "टाइटल") to
            "YT UPLOAD VIDEO: + (centre of the bottom bar) > Video (or swipe the mode to Short / Live / Post) > pick a clip from the gallery > Next (trim/filters/sound) > 'Add details' screen: Title (max 100 chars), Description, Visibility (Public / Unlisted / Private / Schedule), Audience ('Yes, it's made for kids' or 'No, it's not made for kids' is REQUIRED), then Next > Upload. A custom thumbnail needs a verified channel: after upload open YouTube Studio > Content > the video > Edit > the thumbnail. Community post: + > Post > text/photo > Post. NEVER press Upload/Post/Publish until the user clearly told you which file, title and visibility. Check progress: You > Your videos (the bar shows Uploading/Processing/Checking).",
        listOf("delete", "डिलीट", "remove video", "edit video", "एडिट", "change title", "private करो", "make private", "make public", "your videos", "analytics", "एनालिटिक्स", "revenue", "earn", "studio", "स्टूडियो") to
            "YT YOUR VIDEOS: You > 'Your videos' lists uploads. Tap one: Edit (title, description, visibility) > Save; ⋮ > Delete (red, permanent: only when the user clearly asked and named the video). Analytics and comments management live in the YouTube Studio app (package com.google.android.apps.youtube.creator).",
        listOf("setting", "सेटिंग", "dark", "डार्क", "language", "भाषा", "restricted", "break", "bedtime", "remind", "autoplay", "zoom", "double tap") to
            "YT SETTINGS QUICK: You > ⚙. General: Dark theme, App language, Location, Restricted Mode, Remind me to take a break / bedtime reminder. Playback: Autoplay, Double-tap to seek, Zoom to fill, Picture-in-picture. Video quality preferences, Captions, Downloads, Data saving, Notifications. Round toggle at the right of a row = switch; tap once, check that it changed.",
        listOf("buffer", "बफर", "slow", "hang", "not playing", "नहीं चल", "error", "एरर", "problem", "दिक्कत", "crash", "black screen", "no sound", "आवाज़ नहीं", "sound", "volume") to
            "YT FIXES: video stuck/loading: wait 5 s, then gear > Quality > pick a lower quality or Auto, check Wi-Fi/data, go back and reopen. Error or black screen: close and reopen the app, then toggle airplane mode, then Android Settings > Apps > YouTube > Storage > Clear cache (never Clear data without asking: it signs the user out of downloads). No sound: raise the PHONE volume with the side buttons, check the video is not muted (speaker icon) and Silent mode is off. If the app is outdated: Play Store > YouTube > Update."
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

    /** The best 3 matching YouTube procedures for this task (plus links and rules), or "" if nothing matches. */
    fun forTask(job: String): String {
        val t = job.lowercase()
        val best = FLOWS.map { (keys, text) -> keys.count { hit(t, it) } to text }
            .filter { it.first > 0 }
            .sortedByDescending { it.first }
            .take(3)
        val sb = StringBuilder()
        sb.append(LINKS).append('\n')
        for ((_, text) in best) sb.append(text).append('\n')
        sb.append(RULES).append('\n')
        return sb.toString().take(6500)
    }

    /** Studio app know-how, used when the YouTube Studio app is on screen. */
    fun studio(job: String): String {
        val t = job.lowercase()
        val sb = StringBuilder(STUDIO_MAP).append('\n')
        for ((keys, text) in FLOWS) if (keys.any { hit(t, it) } && (text.startsWith("YT YOUR VIDEOS") || text.startsWith("YT UPLOAD"))) sb.append(text).append('\n')
        return sb.toString().take(4500)
    }
}
