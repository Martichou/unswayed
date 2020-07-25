package me.martichou.unswayedphotos.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.TypedValue
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow


fun String.isaValidEmail(): Boolean {
    return Pattern.compile("^([a-z0-9_+]([a-z0-9_+.]*[a-z0-9_+])?)@([a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,6})")
        .matcher(this).matches()
}

fun Float.toDp(resources: Resources): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
}

data class Two(val d1: Any, val d2: Any)

// "imagefile^:^date" => [imagefile, Date()]
fun String.parseToNameAndDate(): Two? {
    val parts = this.split("^:^")
    if (parts.size != 2)
        return null
    val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy,HH:mm:ss", Locale.ENGLISH)
    val date: Date = try {
        formatter.parse(parts[1])!!
    } catch (e: ParseException) {
        Timber.e("Error when parsing string to date $e")
        Date()
    }
    return Two(parts[0], date as Any)
}

// "string", Date() => "string^:^Date()"
fun Two.toStringNameAndDate(): String? {
    if (!(this.d1 is String && this.d2 is Date))
        return null
    val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy,HH:mm:ss", Locale.ENGLISH)
    return this.d1 + "^:^" + formatter.format(this.d2)
}

fun ImageLocal.toStringNameAndDate(): String {
    val formatter: DateFormat = SimpleDateFormat("d-MMM-yyyy,HH:mm:ss", Locale.ENGLISH)
    return this.imgName + "^:^" + formatter.format(this.imgDate)
}

fun String.toBytes(): ByteArray {
    return this.toByteArray(Charsets.UTF_8)
}

fun String.toSha512(): String {
    return HashUtils.sha512(this)
}

fun String.getCompressFormat(): CompressFormat {
    return when (this) {
        "image/jpeg" -> CompressFormat.JPEG
        else -> CompressFormat.PNG
    }
}

fun ImageLocal.createThumbnaill(context: Context): File? {
    val fis = context.contentResolver.openInputStream(this.imgUri!!) ?: return null

    val smallFile =
        File(context.filesDir.absolutePath + File.separator + "thumbnail" + File.separator + this.getUploadName() + "_small")
    val fosSmall = FileOutputStream(smallFile)

    val o = BitmapFactory.Options().apply { this.inJustDecodeBounds = true }
    BitmapFactory.decodeStream(fis, null, o)

    val imageMaxSize = 1024
    var scale = 1
    if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
        scale = 2.0.pow(ceil(ln(imageMaxSize / max(o.outHeight, o.outWidth).toDouble()) / ln(0.5)))
            .toInt()
    }

    val o2 = BitmapFactory.Options().apply { this.inSampleSize = scale }
    val bit = BitmapFactory.decodeStream(fis, null, o2)
    bit?.compress(o.outMimeType.getCompressFormat(), 75, fosSmall)
    fosSmall.flush()
    fosSmall.close()
    fis.close()

    return smallFile
}

fun ImageLocal.thumbnailExists(context: Context): File? {
    val tmpSmall =
        File(context.filesDir.absolutePath + File.separator + "thumbnail" + File.separator + this.getUploadName() + "_small")
    return if (tmpSmall.exists()) tmpSmall else null
}