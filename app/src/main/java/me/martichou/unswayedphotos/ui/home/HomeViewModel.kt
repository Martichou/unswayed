package me.martichou.unswayedphotos.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import me.martichou.unswayedphotos.ui.home.data.ImageRepository

class HomeViewModel @ViewModelInject constructor(respository: ImageRepository) : ViewModel() {

    val imagesList = respository.images

}