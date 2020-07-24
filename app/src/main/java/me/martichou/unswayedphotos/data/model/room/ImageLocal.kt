package me.martichou.unswayedphotos.data.model.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.martichou.unswayedphotos.data.model.recycler.HomeGeneral
import me.martichou.unswayedphotos.util.toStringNameAndDate
import java.util.*

@Entity(tableName = "images")
data class ImageLocal(
    @PrimaryKey val id: Int,
    val imgName: String,
    val imgUri: Uri?,
    val imgDate: Date,
    val backed: Boolean
) : HomeGeneral() {
    override val type: Int
        get() = TYPE_GENERAL

    fun getUploadName(): String = this.toStringNameAndDate()
}