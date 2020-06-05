package me.martichou.unswayed.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.databinding.SignupFragmentBinding

class SignupFragment : Fragment() {

    private lateinit var binding: SignupFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        binding.hdl = this
        return binding.root
    }

    fun performSignup(view: View) {
        // TODO - it's a placeholder for now
        startActivity(Intent(context, MainActivity::class.java)).apply {
            activity?.finish()
        }
    }

}