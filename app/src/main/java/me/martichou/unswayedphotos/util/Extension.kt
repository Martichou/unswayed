package me.martichou.unswayedphotos.util

import android.content.res.Resources
import android.util.TypedValue
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


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
