package com.taipei.attractions.model


import com.google.gson.annotations.SerializedName

data class AttractionsAllModel(
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("total")
    val total: Int = 0
) {
    data class Data(
        @SerializedName("address")
        val address: String = "",
        @SerializedName("distric")
        val distric: String = "",
        @SerializedName("elong")
        val elong: Double = 0.0,
        @SerializedName("email")
        val email: String = "",
        @SerializedName("facebook")
        val facebook: String = "",
        @SerializedName("fax")
        val fax: String = "",
        @SerializedName("id")
        val idData: Int = 0,
        @SerializedName("images")
        val images: List<Image> = listOf(),
        @SerializedName("introduction")
        val introduction: String = "",
        @SerializedName("modified")
        val modified: String = "",
        @SerializedName("months")
        val months: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("nlat")
        val nlat: Double = 0.0,
        @SerializedName("official_site")
        val officialSite: String = "",
        @SerializedName("open_status")
        val openStatus: Int = 0,
        @SerializedName("open_time")
        val openTime: String = "",
        @SerializedName("remind")
        val remind: String = "",
        @SerializedName("staytime")
        val staytime: String = "",
        @SerializedName("tel")
        val tel: String = "",
        @SerializedName("ticket")
        val ticket: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("zipcode")
        val zipcode: String = ""
    ): java.io.Serializable {
        data class Image(
            @SerializedName("ext")
            val ext: String = "",
            @SerializedName("src")
            val src: String = "",
            @SerializedName("subject")
            val subjectImage: String = ""
        ): java.io.Serializable

    }
}