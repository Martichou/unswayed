package me.martichou.unswayed.ui.main

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.MainFragmentBinding
import me.martichou.unswayed.models.DateItem
import me.martichou.unswayed.models.GeneralItem
import me.martichou.unswayed.models.ImageItem
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
        binding.mainRecyclerview.adapter = adapter.apply { setHasStableIds(true) }
        val gridLayoutManager = GridLayoutManager(context, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == 1) 1 else 4
            }
        }
        binding.mainRecyclerview.addItemDecoration(SpacingDecorator(toDP(2f).toInt()))
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

        val callback = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.navigation, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.share -> {
                        // Share...
                        true
                    }
                    R.id.dustbin -> {
                        // Move to dustbin and delete from device if it was backed up
                        // Else simply delete from device with a warning
                        true
                    }
                    R.id.delete -> {
                        // Handle delete from cloud and device
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        }

        (activity as AppCompatActivity).startSupportActionMode(callback).apply {
            this?.title = "1"
        }
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
            while (cursor.moveToNext()) {
                listOfAllImages.add(
                    ImageItem(
                        Uri.withAppendedPath(uriExternal, "" + cursor.getLong(columnIndexID)),
                        Date(cursor.getLong(columnDateID) * 1000)
                    )
                )
            }
            // Now adding the date separator
            var index = -1
            var date: Date
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            while (++index < listOfAllImages.size) {
                if (index == 0) {
                    listOfAllImages.add(0, DateItem((listOfAllImages[0] as ImageItem).imgDate))
                } else {
                    val prevItem = listOfAllImages[index - 1]
                    if (prevItem is ImageItem) {
                        date = (listOfAllImages[index] as ImageItem).imgDate
                        cal1.time = date
                        cal2.time = prevItem.imgDate

                        val sameDay = cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                                cal1[Calendar.YEAR] == cal2[Calendar.YEAR]

                        if (!sameDay) {
                            listOfAllImages.add(index, DateItem(prevItem.imgDate))
                        }
                    }
                }
            }
        }
        query?.close()
        return listOfAllImages
    }

    private fun toDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value,
            resources.displayMetrics
        )
    }

}