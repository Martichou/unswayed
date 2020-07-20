package me.martichou.unswayedphotos.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.martichou.unswayedphotos.databinding.SettingsHomeFragmentBinding

class SettingsHomeFragment : Fragment() {

    private lateinit var binding: SettingsHomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsHomeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

}