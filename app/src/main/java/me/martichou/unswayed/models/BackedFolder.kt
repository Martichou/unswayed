package me.martichou.unswayed.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BackedFolder(
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "folder_id") val folderId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}