package com.example.myphotocollections.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("id") val id: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("url") val url: String,
    @SerializedName("photographer") val photographer: String,
    @SerializedName("photographer_url") val photographerUrl: String,
    @SerializedName("photographer_id") val photographerId: Int,
    @SerializedName("avg_color") val avgColor: String? = "#FFFFFF", // Default color if none provided
    @SerializedName("src") val src: Src,
    @SerializedName("liked") val liked: Boolean = false, // Default to false if not provided
    @SerializedName("alt") val alt: String? = ""
)
