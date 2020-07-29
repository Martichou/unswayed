package me.martichou.unswayedphotos.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import me.martichou.unswayedphotos.models.Zimage

@Dao
interface ImageDaoAsync {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImages(): LiveData<List<Zimage>>

}