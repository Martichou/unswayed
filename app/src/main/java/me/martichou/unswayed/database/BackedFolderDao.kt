package me.martichou.unswayed.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.martichou.unswayed.models.BackedFolder

@Dao
interface BackedFolderDao {

    @Query("SELECT * FROM backedfolder")
    fun getAllBackedFolder(): LiveData<List<BackedFolder>>

    @Insert()
    fun insertBackedFolder(folder: BackedFolder)

}