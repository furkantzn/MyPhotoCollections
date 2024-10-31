package com.example.myphotocollections.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myphotocollections.data.models.Category
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import com.example.myphotocollections.data.services.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

open class NavigationViewModel:ViewModel() {
    private val _trendingPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val trendingPhotos: StateFlow<List<Photo>> = _trendingPhotos

    private var categories: List<Category> = emptyList()
    private val _categoryMap = MutableStateFlow<Map<String,String>>(hashMapOf())
    var categoryMap : StateFlow<Map<String,String>> = _categoryMap

    private var favoritePhotoIds: List<String> = emptyList()
    private val _favoritePhotos = MutableStateFlow<List<Photo>>(emptyList())
    val favoritePhotos: StateFlow<List<Photo>> = _favoritePhotos

    var isTrendingLoaded by mutableStateOf(false)
        private set

    var isCategoriesLoaded by mutableStateOf(false)
        private set

    fun initTrendingPhotos(page: Int = 1, perPage: Int = 10, onComplete: (Boolean) -> Unit) {
        val call = RetrofitInstance.apiService.getTrendingPhotos(page, perPage)
        call.enqueue(object : Callback<PhotosResponse> {
            override fun onResponse(
                call: Call<PhotosResponse>,
                response: Response<PhotosResponse>
            ) {
                if (response.isSuccessful) {
                    val photosResponse = response.body()
                    photosResponse?.photos?.let { addTrendingPhotos(it) }
                    isTrendingLoaded = true
                    onComplete(true)
                } else {
                    isTrendingLoaded = false
                    println("Response was not successful: ${response.code()}")
                    onComplete(false)
                }
            }

            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                isTrendingLoaded = false
                println("Failed to fetch photos: ${t.message}")
                onComplete(false)
            }
        })
    }

    fun addTrendingPhotos(photos:List<Photo>){
        val updatedList = _trendingPhotos.value.toMutableList()
        updatedList.addAll(photos)
        _trendingPhotos.value = updatedList
    }

    //Categories
    fun initCategoriesFromJson(context: Context, onComplete: (Boolean) -> Unit) {
        try {
            val jsonString = getCategoryJsonDataFromAsset(context)
            val type = object : TypeToken<List<Category>>() {}.type
            categories = Gson().fromJson(jsonString,type)
            onComplete(true)
        }catch (e:RuntimeException){
            onComplete(false)
        }

    }

    fun initCategoryPhotos(onComplete: (Boolean) -> Unit){
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
                        isCategoriesLoaded = true
                        onComplete(true)
                        println("Photo added successfully!")
                    } else {
                        println("Response was not successful: ${response.code()}")
                        isCategoriesLoaded = false
                        onComplete(false)
                    }
                }

                override fun onFailure(call: Call<Photo>, t: Throwable) {
                    println("Failed to fetch photo: ${t.message}")
                    isCategoriesLoaded = false
                    onComplete(false)
                }
            })
        }
    }

    private fun getCategoryJsonDataFromAsset(
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

    //Favorites PAge
    fun initFavoritePhotoIds(onComplete: (Boolean) -> Unit) {
        try {
            val json = SharedPrefManager.getString("favorite_photos","")
            val type = object :TypeToken<ArrayList<String>>(){}.type
            favoritePhotoIds = Gson().fromJson(json,type)
            onComplete(true)
        }catch (e:RuntimeException){
            onComplete(false)
        }

    }
    fun initFavoritePhotos(onComplete: (Boolean) -> Unit){
        _favoritePhotos.value = emptyList()
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
        val updatedList = _favoritePhotos.value.toMutableList()
        updatedList.add(photo)
        _favoritePhotos.value = updatedList
    }

    fun allItemsIdentical():Boolean{
        val favoritePhotoIdsFromPhotos = mutableListOf<String>()
        for(photo in _favoritePhotos.value){
            favoritePhotoIdsFromPhotos.add(photo.id.toString())
        }
        return favoritePhotoIds.toList().sorted() == favoritePhotoIdsFromPhotos.toList().sorted()
    }
}