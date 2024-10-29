package com.example.myphotocollections.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import com.example.myphotocollections.data.services.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesPageViewModel:ViewModel() {
    private var favoritePhotoIds: List<String> = emptyList()
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    fun initFavoritePhotos(onComplete: (Boolean) -> Unit) {
        try {
            val json = SharedPrefManager.getString("favorite_photos","")
            val type = object :TypeToken<ArrayList<String>>(){}.type
            favoritePhotoIds = Gson().fromJson(json,type)
            onComplete(true)
        }catch (e:RuntimeException){
            onComplete(false)
        }

    }
    fun initPhotos(onComplete: (Boolean) -> Unit){
        for (photoId in favoritePhotoIds){
            val call = RetrofitInstance.apiService.getPhotoDetail(photoId.toInt())

            call.enqueue(object : Callback<Photo> {
                override fun onResponse(
                    call: Call<Photo>,
                    response: Response<Photo>
                ) {
                    if (response.isSuccessful) {
                        val photo = response.body()
                        if (photo != null) {
                            addPhoto(photo)
                        }
                        onComplete(true)
                        println("Photo added successfully!")
                    } else {
                        println("Response was not successful: ${response.code()}")
                        onComplete(false)
                    }
                }

                override fun onFailure(call: Call<Photo>, t: Throwable) {
                    println("Failed to fetch photo: ${t.message}")
                    onComplete(false)
                }
            })
        }
    }

    fun addPhoto(photo:Photo){
        val updatedList = _photos.value.toMutableList()
        updatedList.add(photo)
        _photos.value = updatedList
    }
}