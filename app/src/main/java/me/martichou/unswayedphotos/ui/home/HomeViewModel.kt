package me.martichou.unswayedphotos.ui.home

import androidx.lifecycle.ViewModel
import me.martichou.unswayedphotos.ui.home.data.ImageRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(respository: ImageRepository) : ViewModel() {

    val imagesList = respository.images

}