package me.martichou.unswayed.ui.folder

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.martichou.unswayed.databinding.FolderFragmentBinding
import me.martichou.unswayed.models.FolderItem

class FolderFragment : Fragment() {

    private lateinit var binding: FolderFragmentBinding
    private lateinit var viewModel: FolderViewModel
    private val adapter = FolderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FolderFragmentBinding.inflate(inflater, container, false)
        binding.folderRecyclerview.adapter = adapter
        binding.folderRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.folderRecyclerview.addItemDecoration(SpacingDecorator(toDP(8f).toInt()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(getAllFolders())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FolderViewModel::class.java)
    }

    private fun getAllFolders(): MutableList<FolderItem> {
        val listOfAllFolder: MutableList<FolderItem> = mutableListOf()
        val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projectionArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        } else {
            arrayOf(MediaStore.Images.Media._ID, "bucket_display_name")
        }
        val query: Cursor? = context?.contentResolver?.query(
            uriExternal,
            projectionArray,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
        )
        query?.let { cursor ->
            val columnIndexID: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val columnBucketID: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            } else {
                cursor.getColumnIndexOrThrow("bucket_display_name")
            }
            while (cursor.moveToNext()) {
                val folderNamed = cursor.getString(columnBucketID)
                if ((listOfAllFolder.filter { (folderName) -> folderName == folderNamed }).isEmpty()) {
                    listOfAllFolder.add(
                        FolderItem(
                            folderNamed,
                            Uri.withAppendedPath(uriExternal, "" + cursor.getLong(columnIndexID))
                        )
                    )
                }
            }
        }
        query?.close()
        return listOfAllFolder
    }

    private fun toDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value,
            resources.displayMetrics
        )
    }

}