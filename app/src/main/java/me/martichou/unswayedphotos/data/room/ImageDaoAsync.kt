package me.martichou.unswayedphotos.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import me.martichou.unswayedphotos.data.model.room.ImageLocal

@Dao
interface ImageDaoAsync {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImages(): LiveData<List<ImageLocal>>

}