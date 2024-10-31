package com.example.myphotocollections.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDetailPageViewModel:ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    var isCategoriesDetailLoaded by mutableStateOf(false)
        private set
    fun initPhotos(query:String,page: Int = 1, perPage: Int = 10, onComplete: (Boolean) -> Unit) {
        val call = RetrofitInstance.apiService.searchPhotos(query,page, perPage)

        call.enqueue(object : Callback<PhotosResponse> {
            override fun onResponse(
                call: Call<PhotosResponse>,
                response: Response<PhotosResponse>
            ) {
                if (response.isSuccessful) {
                    val photosResponse = response.body()
                    photosResponse?.photos?.let { addPhotos(it) }
                    isCategoriesDetailLoaded = true
                    onComplete(true)
                } else {
                    println("Response was not successful: ${response.code()}")
                    isCategoriesDetailLoaded = false
                    onComplete(false)
                }
            }

            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                println("Failed to fetch photos: ${t.message}")
                isCategoriesDetailLoaded = false
                onComplete(false)
            }
        })
    }

    fun addPhotos(photos:List<Photo>){
        val updatedList = _photos.value.toMutableList()
        updatedList.addAll(photos)
        _photos.value = updatedList
    }
}