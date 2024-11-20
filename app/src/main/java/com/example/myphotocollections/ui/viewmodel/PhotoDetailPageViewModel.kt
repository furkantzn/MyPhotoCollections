package com.example.myphotocollections.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.myphotocollections.data.model.Photo
import com.example.myphotocollections.data.repository.PexelsRepository
import com.example.myphotocollections.utils.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class PhotoDetailPageViewModel @Inject constructor(
    private val pexelsRepository: PexelsRepository
) : ViewModel() {
    fun getPhotoDetail(photoId: Int, onComplete: (Boolean, Photo?) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val response = pexelsRepository.getPhotoDetails(photoId)
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
        }
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

    fun downloadImageWithCoil(
        context: Context,
        imageUrl: String,
        fileName: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable

            val bitmap = (result as BitmapDrawable).bitmap

            val path = File(MediaStore.Images.Media.RELATIVE_PATH + "Pictures/MyPhotoCollections")

            if (!path.exists())
                path.mkdirs()

            saveImageToGallery(context, bitmap, fileName) { success, message ->
                onComplete(success, message)
            }
        }
    }

    private fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        onComplete: (Boolean, String) -> Unit
    ) {
        val contentResolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/MyPhotoCollections"
            )
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                onComplete(true, "Image saved successfully!")
            } ?: run {
                onComplete(false, "Error saving image!")
            }
        }
    }

    fun fileExistsInMediaStore(context: Context, fileName: String): Boolean {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val selection =
            "${MediaStore.Images.Media.RELATIVE_PATH}=? AND ${MediaStore.Images.Media.DISPLAY_NAME}=?"
        val selectionArgs = arrayOf("Pictures/MyPhotoCollections/", fileName)

        context.contentResolver.query(collection, projection, selection, selectionArgs, null)
            .use { cursor ->
                return (cursor != null && cursor.moveToFirst())
            }
    }

}