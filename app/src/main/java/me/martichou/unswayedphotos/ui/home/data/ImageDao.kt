package me.martichou.unswayedphotos.ui.home.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import me.martichou.unswayedphotos.data.model.room.ImageLocal

@Dao
interface ImageDao {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImages(): LiveData<List<ImageLocal>>

}