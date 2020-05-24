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
import me.martichou.unswayed.models.ImageObject
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
                return when (adapter.getItemViewType(position)) {
                    1 -> 1
                    else -> 4
                }
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

    private fun getAllImages(): MutableList<ImageObject> {
        val listOfAllImages: MutableList<ImageObject> = mutableListOf()
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
                    ImageObject(
                        Uri.withAppendedPath(
                            uriExternal,
                            "" + cursor.getLong(columnIndexID)
                        ),
                        date,
                        false
                    )
                )
            }
            var index = -1
            while (++index < listOfAllImages.size) {
                if (index == 0) {
                    listOfAllImages.add(0,
                        ImageObject(
                            null,
                            listOfAllImages[0].imgDate,
                            true
                        )
                    )
                } else {
                    val prevImg = listOfAllImages[index - 1]

                    cal1.time = listOfAllImages[index].imgDate
                    cal2.time = prevImg.imgDate

                    val sameDay = cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                            cal1[Calendar.YEAR] == cal2[Calendar.YEAR]

                    if (!sameDay) {
                        listOfAllImages.add(index,
                            ImageObject(
                                null,
                                listOfAllImages[index].imgDate,
                                true
                            )
                        )
                    }
                }
            }
        }
        query?.close()
        return listOfAllImages
    }

}