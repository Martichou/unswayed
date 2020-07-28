package me.martichou.unswayedphotos.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import java.util.*

fun getImages(context: Context, selectFolder: List<String>? = null): MutableList<ImageLocal> {
    val listOfAllImages: MutableList<ImageLocal> = mutableListOf()
    val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.DISPLAY_NAME
    )
    val selection = if (selectFolder != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " IN ('${selectFolder.joinToString { it }}')"
        } else {
            "bucket_display_name IN ('${selectFolder.joinToString { it }}')"
        }
    } else {
        null
    }
    val query: Cursor? = context.contentResolver?.query(
        uriExternal,
        projection,
        selection,
        null,
        MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
    )
    query?.let { cursor ->
        val cIndexID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val cDateID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
        val cNameID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            listOfAllImages.add(
                ImageLocal(
                    id = Two(
                        cursor.getString(cNameID),
                        Date(cursor.getLong(cDateID) * 1000)
                    ).toStringNameAndDate()!!,
                    imgName = cursor.getString(cNameID),
                    imgUri = Uri.withAppendedPath(
                        uriExternal,
                        "" + cursor.getLong(cIndexID)
                    ),
                    imgDate = Date(cursor.getLong(cDateID) * 1000),
                    backed = false
                )
            )
        }
    }
    query?.close()
    return listOfAllImages
}