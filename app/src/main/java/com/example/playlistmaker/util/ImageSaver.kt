package com.example.playlistmaker.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.example.playlistmaker.ui.library.playlists.playlist_creator.PlaylistCreatorFragment
import java.io.File
import java.io.FileOutputStream

object ImageSaver {

    fun saveImage(activity: FragmentActivity, uri: Uri, fileName: String): String {
        val filePath = File(
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PlaylistCreatorFragment.ALBUM_PICTURES_DIR
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, fileName)

        val inputStream = activity.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        val isSaveSuccess = BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return if (isSaveSuccess) {
            fileName
        } else {
            ""
        }
    }

    fun getImage(context: Context, imgDir: String, imgName: String): File {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imgDir)
        return File(filePath, imgName)
    }
}