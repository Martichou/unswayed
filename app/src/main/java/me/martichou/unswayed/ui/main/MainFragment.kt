package me.martichou.unswayed.ui.main

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.martichou.unswayed.databinding.MainFragmentBinding
import me.martichou.unswayed.models.*
import timber.log.Timber
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private val adapter = ImagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.mainRecyclerview.adapter = adapter
        val gridLayoutManager = GridLayoutManager(context, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == 1) 1 else 4
            }
        }
        binding.mainRecyclerview.layoutManager = gridLayoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(getAllImages())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun getAllImages(): MutableList<GeneralItem> {
        val listOfAllImages: MutableList<GeneralItem> = mutableListOf()
        val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val query: Cursor? = context?.contentResolver?.query(
            uriExternal,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_MODIFIED),
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
        )
        query?.let { cursor ->
            val columnIndexID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val columnDateID: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            while (cursor.moveToNext()) {
                val date = Date(cursor.getLong(columnDateID) * 1000)
                listOfAllImages.add(
                    ImageItem(
                        Uri.withAppendedPath(uriExternal, "" + cursor.getLong(columnIndexID)),
                        date
                    )
                )
            }
            // Now adding the date separator
            var index = -1
            while (++index < listOfAllImages.size) {
                if (index == 0) {
                    listOfAllImages.add(0, DateItem((listOfAllImages[0] as ImageItem).imgDate))
                } else {
                    val prevItem = listOfAllImages[index - 1]
                    if (prevItem is ImageItem) {
                        val date = (listOfAllImages[index] as ImageItem).imgDate
                        cal1.time = date
                        cal2.time = prevItem.imgDate

                        val sameDay = cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                                cal1[Calendar.YEAR] == cal2[Calendar.YEAR]

                        if (!sameDay) {
                            listOfAllImages.add(index, DateItem(date))
                        }
                    }
                }
            }
        }
        query?.close()
        return listOfAllImages
    }

}