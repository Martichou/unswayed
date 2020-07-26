package me.martichou.unswayedphotos.ui.home

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import me.martichou.unswayedphotos.databinding.HomeFragmentBinding
import me.martichou.unswayedphotos.di.Injectable
import me.martichou.unswayedphotos.di.injectViewModel
import me.martichou.unswayedphotos.ui.home.adapter.HomeAdapter
import me.martichou.unswayedphotos.util.HomeSpacingDecorator
import me.martichou.unswayedphotos.util.Two
import me.martichou.unswayedphotos.util.toDp
import me.martichou.unswayedphotos.util.toStringNameAndDate
import timber.log.Timber
import java.security.KeyStore
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var keyStore: KeyStore

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = HomeFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = HomeAdapter()
        val gridLayoutManager = GridLayoutManager(context, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == 1) 1 else 4
            }
        }
        binding.mainRecyclerview.addItemDecoration(HomeSpacingDecorator(2f.toDp(resources).toInt()))
        binding.mainRecyclerview.layoutManager = gridLayoutManager
        binding.mainRecyclerview.adapter = adapter

        subscribeUi(binding, adapter)

        return binding.root
    }

    private fun subscribeUi(binding: HomeFragmentBinding, adapter: HomeAdapter) {
        viewModel.imagesList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    Timber.d("Success")
                }
                Result.Status.LOADING -> {
                    Timber.d("Loading")
                }
                Result.Status.ERROR -> {
                    Timber.d("Error ${result.message}")
                }
            }
        })

        val imgList = getAllImages()
        imgList.forEach {
            Timber.d(it.toString())
        }
    }

    private fun getAllImages(): MutableList<ImageLocal> {
        val listOfAllImages: MutableList<ImageLocal> = mutableListOf()
        val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " IN ('Camera')"
        } else {
            "bucket_display_name IN ('Camera')"
        }
        val query: Cursor? = context?.contentResolver?.query(
            uriExternal,
            projection,
            selection,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
        )
        query?.let { cursor ->
            val cIndexID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val cDateID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val cNameID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                listOfAllImages.add(
                    ImageLocal(
                        id = Two(
                            cursor.getString(cNameID),
                            Date(cursor.getLong(cDateID) * 1000)
                        ).toStringNameAndDate()!!,
                        imgName = cursor.getString(cNameID),
                        imgUri = Uri.withAppendedPath(
                            uriExternal,
                            "" + cursor.getLong(cIndexID)
                        ),
                        imgDate = Date(cursor.getLong(cDateID) * 1000),
                        backed = false
                    )
                )
            }
        }
        query?.close()
        return listOfAllImages
    }

}