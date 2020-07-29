package me.martichou.unswayedphotos.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.martichou.unswayedphotos.models.Zimage

@Dao
interface ImageDaoSync {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImagesSync(): List<Zimage>

    @Insert(onConflict = REPLACE)
    fun insertImage(imageLocal: Zimage)

}