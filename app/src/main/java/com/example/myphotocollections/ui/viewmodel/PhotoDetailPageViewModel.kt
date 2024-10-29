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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoDetailPageViewModel:ViewModel() {
    fun getPhotoDetail(photoId:Int, onComplete: (Boolean,Photo?) -> Unit) {
        val call = RetrofitInstance.apiService.getPhotoDetail(photoId)

        call.enqueue(object : Callback<Photo> {
            override fun onResponse(
                call: Call<Photo>,
                response: Response<Photo>
            ) {
                if (response.isSuccessful) {
                    val photo = response.body()
                    if(photo!=null){
                        onComplete(true,photo)
                    }
                } else {
                    println("Response was not successful: ${response.code()}")
                    onComplete(false,null)
                }
            }

            override fun onFailure(call: Call<Photo>, t: Throwable) {
                println("Failed to fetch photos: ${t.message}")
                onComplete(false,null)
            }
        })
    }

}