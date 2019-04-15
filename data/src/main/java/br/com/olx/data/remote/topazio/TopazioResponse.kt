package br.com.olx.data.remote.topazio

import com.google.gson.annotations.SerializedName

data class TopazioResponse(
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("ads")
    val ads: Ads
)

data class Ads(
    @SerializedName("pagination")
    val pagination: String?,
    @SerializedName("ads")
    val ads: List<Ads1>
)

data class Ads1(
        @SerializedName("listId")
    val listId: Int?,
        @SerializedName("rankId")
    val rankId: String?,
        @SerializedName("lastBumpAgeSecs")
    val lastBumpAgeSecs: String?,
        @SerializedName("isFavorited")
    val isFavorited: Boolean?,
        @SerializedName("subject")
    val subject: String?,
        @SerializedName("origListTime")
    val origListTime: Long?,
        @SerializedName("priceValue")
    val priceValue: String?,
        @SerializedName("oldPrice")
    val oldPrice: String?,
        @SerializedName("professionalAd")
    val professionalAd: Boolean?,
        @SerializedName("featured")
    val featured: List<Any?>,
        @SerializedName("location")
    val location: Location,
        @SerializedName("images")
    val images: List<Image?>,
        @SerializedName("properties")
    val properties: List<Properties>,
        @SerializedName("thumbnail")
    val thumbnail: Thumbnail?,
        @SerializedName("user")
    val user: User?,
        @SerializedName("phone")
    val phone: Phone?,
        @SerializedName("adReply")
    val adReply: String?
)

data class Location(
    @SerializedName("neighbourhood")
    val neighbourhood: String?,
    @SerializedName("municipality")
    val municipality: String?,
    @SerializedName("uf")
    val uf: String?
)

data class Properties(
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value: String?
)

data class Image(
    @SerializedName("baseUrl")
    val baseUrl: String?,
    @SerializedName("path")
    val path: String?
)

data class Thumbnail(
    @SerializedName("baseUrl")
    val baseUrl: String?,
    @SerializedName("path")
    val path: String?
)

data class User(
    @SerializedName("name")
    val name: String?,
    @SerializedName("accountId")
    val accountId: Int?
)

data class Phone(
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("phoneHidden")
    val phoneHidden: Boolean?,
    @SerializedName("phoneVerified")
    val phoneVerified: Boolean?
)
