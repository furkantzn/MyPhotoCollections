package com.example.myphotocollections.ui.viewmodel

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.myphotocollections.data.models.Photo
import com.example.myphotocollections.data.models.PhotosResponse
import com.example.myphotocollections.data.retrofit.RetrofitInstance
import com.example.myphotocollections.data.services.SharedPrefManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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

        // Set up metadata
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/MyPhotoCollections"
            ) // Directory to save in
        }

        // Insert into MediaStore
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            // Write the bitmap data to the output stream
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
        // Specify the collection
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Set up the query to search for the file
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val selection =
            "${MediaStore.Images.Media.RELATIVE_PATH}=? AND ${MediaStore.Images.Media.DISPLAY_NAME}=?"
        val selectionArgs = arrayOf("Pictures/MyPhotoCollections/", fileName)

        // Query MediaStore
        context.contentResolver.query(collection, projection, selection, selectionArgs, null)
            .use { cursor ->
                // Check if a result was found
                return (cursor != null && cursor.moveToFirst())
            }
    }

}