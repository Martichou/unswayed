package me.martichou.unswayedphotos.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.martichou.unswayedphotos.models.Zimage

@Dao
interface ImageDaoAsync {

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImagesLiveData(): LiveData<List<Zimage>>

    @Query("SELECT * FROM images ORDER BY imgDate DESC")
    fun getImagesFlow(): Flow<List<Zimage>>

}