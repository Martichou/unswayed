package me.martichou.unswayedphotos.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream

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

fun ImageLocal.encrypt(
    context: Context,
    secretKey: Key,
    associatedData: ByteArray? = null
): Two? {
    if (imgUri == null) return null
    // Original inputStream
    val fis = context.contentResolver.openInputStream(imgUri) ?: return null
    // Create thumbnail or get it, and set fisSmall
    val fisSmall = FileInputStream(getThumbnail(context) ?: createThumbnail(context))

    // Output files

    val path = context.cacheDir.path + File.separator + "tmpFiles" + File.separator

    val dir = File(path)
    if (!dir.exists()) dir.mkdir()

    val tmpEncFile = File(path + getUploadName())
    val tmpSmallEncFile = File(path + getUploadName() + "_small")

    for (x in 0..1) {
        if (x == 0 && tmpEncFile.exists()) {
            fis.close()
            continue
        } else if (x == 1 && tmpSmallEncFile.exists()) {
            fisSmall.close()
            continue
        }

        val targetFos =
            if (x == 0) FileOutputStream(tmpEncFile) else FileOutputStream(tmpSmallEncFile)
        val targetFis = if (x == 0) fis else fisSmall

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        associatedData?.let { cipher.updateAAD(it) }

        val cos = CipherOutputStream(targetFos, cipher)
        var b: Int
        val d = ByteArray(1048576)
        while (targetFis.read(d).also { b = it } != -1) {
            cos.write(d, 0, b)
        }
        cos.flush()
        cos.close()
        targetFos.close()
        targetFis.close()
    }

    return Two(tmpEncFile, tmpSmallEncFile)
}