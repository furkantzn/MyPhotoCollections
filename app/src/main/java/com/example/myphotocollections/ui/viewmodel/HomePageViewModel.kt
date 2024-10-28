package com.example.myphotocollections.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageViewModel : ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos
    fun initPhotos(page: Int = 1, perPage: Int = 15, onComplete: (Boolean) -> Unit) {
        val call = RetrofitInstance.apiService.getTrendingPhotos(page, perPage)

        call.enqueue(object : Callback<PhotosResponse> {
            override fun onResponse(
                call: Call<PhotosResponse>,
                response: Response<PhotosResponse>
            ) {
                if (response.isSuccessful) {
                    onComplete(true)
                    val photosResponse = response.body()
                    photosResponse?.photos?.let { addPhotos(it) }
                } else {
                    println("Response was not successful: ${response.code()}")
                    onComplete(false)
                }
            }

            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                println("Failed to fetch photos: ${t.message}")
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