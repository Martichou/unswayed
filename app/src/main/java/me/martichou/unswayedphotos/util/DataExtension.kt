package me.martichou.unswayedphotos.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

fun ImageLocal.createThumbnail(context: Context): File? {
    if (imgUri == null) return null
    val path = context.cacheDir.path + File.separator + "thumbnails" + File.separator

    val dir = File(path)
    if (!dir.exists()) dir.mkdir()

    val smallFile = File(path + this.getUploadName() + "_small")
    val fosSmall = FileOutputStream(smallFile)

    var fis: InputStream = context.contentResolver.openInputStream(this.imgUri) ?: return null
    val bitBounds = BitmapFactory.Options().apply { this.inJustDecodeBounds = true }
    BitmapFactory.decodeStream(fis, null, bitBounds).also { fis.close() }

    val imgMSize = 768
    val scale = if (bitBounds.outHeight > imgMSize || bitBounds.outWidth > imgMSize) {
        2.0.pow(
            ceil(ln(imgMSize / max(bitBounds.outHeight, bitBounds.outWidth).toDouble()) / ln(0.5))
        ).toInt()
    } else {
        1
    }

    fis = context.contentResolver.openInputStream(this.imgUri) ?: return null
    val bitSample = BitmapFactory.Options().apply { this.inSampleSize = scale }
    val bitmap = BitmapFactory.decodeStream(fis, null, bitSample).also { fis.close() }
    bitmap?.compress(Bitmap.CompressFormat.PNG, 95, fosSmall)
    fosSmall.flush()
    fosSmall.close()

    return smallFile
}

fun ImageLocal.getThumbnail(context: Context): File? {
    val tmpSmall =
        File(context.filesDir.absolutePath + File.separator + "thumbnail" + File.separator + this.getUploadName() + "_small")
    return if (tmpSmall.exists()) tmpSmall else null
}

fun ImageLocal.thumbnailExists(context: Context): Boolean {
    return File(context.filesDir.absolutePath + File.separator + "thumbnail" + File.separator + this.getUploadName() + "_small").exists()
}

fun String.thumbnailExists(context: Context): Boolean {
    return File(context.filesDir.absolutePath + File.separator + "thumbnail" + File.separator + this).exists()
}