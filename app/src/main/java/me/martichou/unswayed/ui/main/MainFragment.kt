package me.martichou.unswayed.ui.main

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.MainFragmentBinding
import me.martichou.unswayed.models.DateItem
import me.martichou.unswayed.models.GeneralItem
import me.martichou.unswayed.models.ImageItem
import timber.log.Timber
import java.io.File
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mtracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null
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
        binding.mainRecyclerview.addItemDecoration(SpacingDecorator(toDP(2f).toInt()))
        binding.mainRecyclerview.layoutManager = gridLayoutManager
        mtracker = SelectionTracker.Builder(
            "mySelection",
            binding.mainRecyclerview,
            StableIdKeyProvider(binding.mainRecyclerview),
            MyItemDetailsLookup(binding.mainRecyclerview),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = mtracker
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
                mode?.menuInflater?.inflate(R.menu.contextual_main, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
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
                        mtracker.selection.reversed().forEach {
                            Timber.d("Idk $it")
                            adapter.getItemAt(it.toInt())?.let { img ->
                                Timber.d("Idk 2 $it")
                                img.imgUri?.let {
                                    // USE SAF?
                                }
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
                mtracker.clearSelection()
            }
        }

        mtracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val items = mtracker.selection!!.size()
                    if (items > 0) {
                        if (actionMode == null) {
                            actionMode =
                                (activity as AppCompatActivity).startSupportActionMode(callback)
                                    .apply {
                                        this?.title = items.toString()
                                    }
                        } else {
                            actionMode?.title = items.toString()
                        }
                    } else {
                        actionMode?.finish()
                    }
                }
            })
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

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            val vh = recyclerView.getChildViewHolder(view)
            // Workaround for now, will need to find a way to avoid user selecting viewType 2
            return if (vh is ImagesAdapter.ViewHolder) {
                vh.getItemDetails()
            } else {
                null
            }
        }
        return null
    }
}