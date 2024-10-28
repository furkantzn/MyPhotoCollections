package com.example.myphotocollections.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myphotocollections.data.models.Category
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CategoriesPageViewModel:ViewModel() {
    private var categories: List<Category> = emptyList()
    private val _categoryMap = MutableStateFlow<Map<String,String>>(hashMapOf())
    var categoryMap : StateFlow<Map<String,String>> = _categoryMap
    fun initCategoriesFromJson(context: Context,onComplete: (Boolean) -> Unit) {
        try {
            val jsonString = getJsonDataFromAsset(context)
            val type = object : TypeToken<List<Category>>() {}.type
            categories = Gson().fromJson(jsonString,type)
            onComplete(true)
        }catch (e:RuntimeException){
            onComplete(false)
        }

    }

    fun initPhotos(onComplete: (Boolean) -> Unit){
        val updatedCategoryMap = _categoryMap.value.toMutableMap()
        for (category in categories){
            val call = RetrofitInstance.apiService.getPhotoDetail(category.photoId)

            call.enqueue(object : Callback<Photo> {
                override fun onResponse(
                    call: Call<Photo>,
                    response: Response<Photo>
                ) {
                    if (response.isSuccessful) {
                        val photo = response.body()
                        if (photo != null) {
                            updatedCategoryMap[category.name] = photo.src.portrait
                            _categoryMap.value = updatedCategoryMap.toMap()
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

    private fun getJsonDataFromAsset(
        context: Context
    ): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("photo_categories.json").bufferedReader().use {
                it.readText()
            }
        } catch (exp: IOException) {
            exp.printStackTrace()
            return null
        }

        return jsonString
    }
}