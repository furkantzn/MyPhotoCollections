package com.example.myphotocollections.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myphotocollections.data.model.Photo
import com.example.myphotocollections.data.repository.PexelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoryDetailPageViewModel @Inject constructor(
    private val pexelsRepository: PexelsRepository
):ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    var isCategoriesDetailLoaded by mutableStateOf(false)
        private set
    fun initPhotos(query:String,page: Int = 1, perPage: Int = 10, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val response = pexelsRepository.fetchPhotos(query,page, perPage)
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
        }
    }

    private fun addPhotos(photos:List<Photo>){
        val updatedList = _photos.value.toMutableList()
        updatedList.addAll(photos)
        _photos.value = updatedList
    }
}