package me.martichou.unswayedphotos.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.martichou.unswayedphotos.AuthActivity
import me.martichou.unswayedphotos.R
import me.martichou.unswayedphotos.databinding.SettingsDialogBinding
import me.martichou.unswayedphotos.util.TokenManager
import me.martichou.unswayedphotos.util.toDp
import java.security.KeyStore
import javax.inject.Inject

@AndroidEntryPoint
class SettingsDialog : DialogFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var keyStore: KeyStore

    private lateinit var binding: SettingsDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.SettingsDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsDialogBinding.inflate(inflater, container, false).apply {
            hdl = this@SettingsDialog
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            it.attributes.y = 60f.toDp(resources).toInt()
            it.setGravity(Gravity.TOP)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        Glide.with(this)
            .load(
                "https://eu.ui-avatars.com/api/?name=${sharedPreferences.getString(
                    "user_email", "Unswayed Photo"
                )}&rounded=true&size=128"
            ).diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(binding.profileImage.measuredWidth, binding.profileImage.measuredWidth)
            .thumbnail(0.1f)
            .into(binding.profileImage)
    }

    fun View.close() {
        dialog?.dismiss()
    }

    fun View.gotoSettings() {
        findNavController().navigate(R.id.settings_activity)
    }

    fun View.logout() {
        CoroutineScope(Dispatchers.IO).launch {
            keyStore.deleteEntry("aesKey")
            tokenManager.deleteToken()
            Glide.get(context).clearDiskCache()
            sharedPreferences.edit().remove("user_email").apply()
            withContext(Dispatchers.Main) {
                Glide.get(context).clearMemory()
                startActivity(Intent(context, AuthActivity::class.java)).also {
                    activity?.finish()
                }
            }
        }
    }

}