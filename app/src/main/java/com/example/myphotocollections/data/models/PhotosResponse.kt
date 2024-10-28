package com.example.myphotocollections.data.models

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("photos") val photos: List<Photo>,
    @SerializedName("next_page") val nextPage: String?
)
