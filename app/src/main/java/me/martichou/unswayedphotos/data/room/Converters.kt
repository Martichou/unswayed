package me.martichou.unswayedphotos.data.room

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun datefromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun uriTostring(uri: Uri): String = uri.toString()

    @TypeConverter
    fun uriFromString(value: String): Uri = Uri.parse(value)
}