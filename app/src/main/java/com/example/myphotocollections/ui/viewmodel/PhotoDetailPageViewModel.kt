package com.example.myphotocollections.ui.viewmodel

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import com.example.myphotocollections.data.services.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoDetailPageViewModel : ViewModel() {
    fun getPhotoDetail(photoId: Int, onComplete: (Boolean, Photo?) -> Unit) {
        val call = RetrofitInstance.apiService.getPhotoDetail(photoId)

        call.enqueue(object : Callback<Photo> {
            override fun onResponse(
                call: Call<Photo>,
                response: Response<Photo>
            ) {
                if (response.isSuccessful) {
                    val photo = response.body()
                    if (photo != null) {
                        onComplete(true, photo)
                    }
                } else {
                    println("Response was not successful: ${response.code()}")
                    onComplete(false, null)
                }
            }

            override fun onFailure(call: Call<Photo>, t: Throwable) {
                println("Failed to fetch photos: ${t.message}")
                onComplete(false, null)
            }
        })
    }

    fun getListFromPref(): List<Int> {
        val json = SharedPrefManager.getString("favorite_photos", "")
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Int>>() {}.type
            return Gson().fromJson(json, type)
        }
        return emptyList()
    }

    fun setFavoritePhotoIdsToPref(favoritePhotoIds: List<Int>, onComplete: (Boolean) -> Unit) {
        try {
            val gson = Gson()
            val json = gson.toJson(favoritePhotoIds)//converting list to Json
            SharedPrefManager.putString("favorite_photos", json)
            onComplete(true)
        } catch (e: RuntimeException) {
            println("Failed to write to pref!")
            onComplete(false)
        }
    }

}