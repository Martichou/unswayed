package me.martichou.unswayed.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.martichou.unswayed.database.AppDatabase

class MainVMFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(db) as T
    }
}