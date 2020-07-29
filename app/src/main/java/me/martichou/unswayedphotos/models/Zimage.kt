package me.martichou.unswayedphotos.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.martichou.unswayedphotos.models.rv.HomeGeneral
import me.martichou.unswayedphotos.util.toStringNameAndDate
import java.util.*

@Entity(tableName = "images")
data class Zimage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imgName: String,
    val imgUri: Uri?,
    val imgDate: Date,
    var backed: Boolean
) : HomeGeneral() {
    override val type: Int
        get() = TYPE_GENERAL

    fun getUploadName(): String = this.toStringNameAndDate()
}