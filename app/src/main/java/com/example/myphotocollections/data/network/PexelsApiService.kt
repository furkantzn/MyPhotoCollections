package com.example.myphotocollections.data.network

import com.example.myphotocollections.BuildConfig
import com.example.myphotocollections.data.model.Photo
import com.example.myphotocollections.data.model.PhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApiService {
    @GET("v1/search")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apiKey: String? = BuildConfig.API_KEY
    ): Call<PhotosResponse>

    @GET("v1/curated")
    fun getTrendingPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apiKey: String? = BuildConfig.API_KEY
    ): Call<PhotosResponse>

    @GET("v1/photos/{id}")
    fun getPhotoDetail(
        @Path("id") photoId: Int,
        @Header("Authorization") apiKey: String? = BuildConfig.API_KEY
    ): Call<Photo>
}