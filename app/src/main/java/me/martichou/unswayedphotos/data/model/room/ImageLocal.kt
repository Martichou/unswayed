package me.martichou.unswayedphotos.data.model.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import me.martichou.unswayedphotos.data.model.recycler.HomeGeneral
import java.util.*

@Entity(tableName = "images")
data class ImageLocal(
    @PrimaryKey val id: Int,
    val imgUri: Uri?,
    val imgDate: Date
) : HomeGeneral() {
    override val type: Int
        get() = TYPE_GENERAL
}