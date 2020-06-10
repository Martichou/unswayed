package me.martichou.unswayed.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.SettingsDialogBinding
import me.martichou.unswayed.utils.toDP

class SettingsPopup : DialogFragment() {

    private lateinit var binding: SettingsDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.SettingsPopup)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsDialogBinding.inflate(inflater, container, false)
        binding.closing.setOnClickListener {
            dialog?.dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            it.attributes.y = toDP(60f, resources).toInt()
            it.setGravity(Gravity.TOP)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        Glide.with(this)
            .load("https://avatars1.githubusercontent.com/u/23138751?s=460&u=f5eefef76889be5eae5ba43b2941224b066edcea&v=4")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .override(binding.profilePic.measuredWidth, binding.profilePic.measuredWidth)
            .thumbnail(0.1f)
            .error(R.drawable.placeholder)
            .into(binding.profilePic)
    }

}