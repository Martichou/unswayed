package me.martichou.unswayed.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.martichou.unswayed.database.AppDatabase
import me.martichou.unswayed.models.BackedFolder

class MainViewModel(db: AppDatabase) : ViewModel() {
    val foldersList: LiveData<List<BackedFolder>> = db.backedFolderDao().getAllBackedFolder()
}