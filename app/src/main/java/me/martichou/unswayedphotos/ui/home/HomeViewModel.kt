package me.martichou.unswayedphotos.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import me.martichou.unswayedphotos.ui.home.data.DataRepository

class HomeViewModel @ViewModelInject constructor(respository: DataRepository) : ViewModel() {

    val imagesList = respository.images

}