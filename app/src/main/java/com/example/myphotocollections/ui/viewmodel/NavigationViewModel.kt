package com.example.myphotocollections.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myphotocollections.data.model.Category
import com.example.myphotocollections.data.model.Photo
import com.example.myphotocollections.data.repository.PexelsRepository
import com.example.myphotocollections.utils.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
open class NavigationViewModel @Inject constructor(
    private val pexelsRepository: PexelsRepository
):ViewModel() {
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
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val response = pexelsRepository.fetchTrendingPhotos(page,perPage)
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
        }
    }

    private fun addTrendingPhotos(photos:List<Photo>){
        val updatedList = _trendingPhotos.value.toMutableList()
        updatedList.addAll(photos)
        _trendingPhotos.value = updatedList
    }

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
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    val response = pexelsRepository.getPhotoDetails(category.photoId)
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
            }
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
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    val response = pexelsRepository.getPhotoDetails(photoId.toInt())
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
            }
        }
    }

    private fun addPhoto(photo:Photo){
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