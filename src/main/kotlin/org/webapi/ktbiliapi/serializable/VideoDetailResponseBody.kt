package org.webapi.ktbiliapi.serializable

import kotlinx.serialization.Serializable
import org.webapi.ktbiliapi.serializable.common.Dimension

@Serializable
data class VideoDetailResponseBody(
    val code: Int,
    val data: VideoDetailData,
    val message: String,
    val ttl: Int
) : CommonResponseBody() {
    init {
        iCode = code
        iMessage = message
    }
}

@Serializable
data class VideoDetailData(
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val copyright: Int,
    val ctime: Int,
    val desc: String,
    val desc_v2: List<DescV2>,
    val dimension: Dimension,
    val duration: Int,
    val dynamic: String,
    val honor_reply: HonorReply? = null,
    val is_chargeable_season: Boolean,
    val is_season_display: Boolean,
    val is_story: Boolean,
    val like_icon: String,
    val mission_id: Long? = null,
    val no_cache: Boolean,
    val owner: Owner,
    val pages: List<Page>,
    val pic: String,
    val premiere: String? = null,
    val pubdate: Long,
    val rights: Rights,
    val staff: List<Staff>? = null,
    val stat: Stat,
    val state: Int,
    val subtitle: Subtitle,
    val teenage_mode: Int,
    val tid: Long,
    val title: String,
    val tname: String,
    val user_garb: UserGarb,
    val videos: Int
)

@Serializable
data class DescV2(
    val biz_id: Long,
    val raw_text: String,
    val type: Int
)

@Serializable
data class HonorReply(
    val honor: List<Honor>? = null
)

@Serializable
data class Owner(
    val face: String,
    val mid: Long,
    val name: String
)

@Serializable
data class Page(
    val cid: Long,
    val dimension: Dimension,
    val duration: Int,
    val from: String,
    val page: Int,
    val part: String,
    val vid: String,
    val weblink: String
)

@Serializable
data class Rights(
    val arc_pay: Int,
    val autoplay: Int,
    val bp: Int,
    val clean_mode: Int,
    val download: Int,
    val elec: Int,
    val free_watch: Int,
    val hd5: Int,
    val is_360: Int,
    val is_cooperation: Int,
    val is_stein_gate: Int,
    val movie: Int,
    val no_background: Int,
    val no_reprint: Int,
    val no_share: Int,
    val pay: Int,
    val ugc_pay: Int,
    val ugc_pay_preview: Int
)

@Serializable
data class Staff(
    val face: String,
    val follower: Int,
    val label_style: Int,
    val mid: Long,
    val name: String,
    val official: Official,
    val title: String,
    val vip: Vip
)

@Serializable
data class Stat(
    val aid: Long,
    val argue_msg: String,
    val coin: Int,
    val danmaku: Int,
    val dislike: Int,
    val evaluation: String,
    val favorite: Int,
    val his_rank: Int,
    val like: Int,
    val now_rank: Int,
    val reply: Int,
    val share: Int,
    val view: Int
)

@Serializable
data class Subtitle(
    val allow_submit: Boolean,
    val list: List<SubTitleListItem?>?
)

@Serializable
data class SubTitleListItem(
    val id: Long,
    val lan: String,
    val lan_doc: String,
    val is_lock: Boolean,
    val author_mid: Long,
    val subtitle_url: String,
    val author: Author
)

@Serializable
data class Author(
    val mid: Long,
    val name: String,
    val sex: String,
    val face: String,
    val sign: String,
    val rank: Int,
    val birthday: Int,
    val is_fake_account: Int,
    val is_deleted: Int
)

@Serializable
data class UserGarb(
    val url_image_ani_cut: String
)

@Serializable
data class Honor(
    val aid: Long,
    val desc: String,
    val type: Int,
    val weekly_recommend_num: Int
)

@Serializable
data class Official(
    val desc: String,
    val role: Int,
    val title: String,
    val type: Int
)

@Serializable
data class Vip(
    val avatar_subscript: Int,
    val avatar_subscript_url: String,
    val due_date: Long,
    val label: Label,
    val nickname_color: String,
    val role: Int,
    val status: Int,
    val theme_type: Int,
    val tv_vip_pay_type: Int,
    val tv_vip_status: Int,
    val type: Int,
    val vip_pay_type: Int
)

@Serializable
data class Label(
    val bg_color: String,
    val bg_style: Int,
    val border_color: String,
    val img_label_uri_hans: String,
    val img_label_uri_hans_static: String,
    val img_label_uri_hant: String,
    val img_label_uri_hant_static: String,
    val label_theme: String,
    val path: String,
    val text: String,
    val text_color: String,
    val use_img_label: Boolean
)