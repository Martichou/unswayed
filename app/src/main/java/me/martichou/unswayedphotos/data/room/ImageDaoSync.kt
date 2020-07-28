package me.martichou.unswayedphotos.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.martichou.unswayedphotos.data.model.room.ImageLocal

@Dao
interface ImageDaoSync {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImagesSync(): List<ImageLocal>

    @Insert(onConflict = REPLACE)
    fun insertImage(imageLocal: ImageLocal)

}