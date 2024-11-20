package com.example.myphotocollections.data.repository

import com.example.myphotocollections.data.model.Photo
import com.example.myphotocollections.data.model.PhotosResponse
import com.example.myphotocollections.data.network.PexelsApiService
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class PexelsRepository @Inject constructor(
    private val apiService: PexelsApiService
) {
    suspend fun fetchPhotos(query: String, page: Int, perPage: Int): Response<PhotosResponse> {
        return apiService.searchPhotos(query = query, page = page, perPage = perPage).awaitResponse()
    }

    suspend fun fetchTrendingPhotos(page: Int, perPage: Int): Response<PhotosResponse> {
        return apiService.getTrendingPhotos(page = page, perPage = perPage).awaitResponse()
    }

    suspend fun getPhotoDetails(photoId: Int):Response<Photo> {
       return apiService.getPhotoDetail(photoId).awaitResponse()
    }
}