package me.martichou.unswayed.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.databinding.SigninFragmentBinding

class SigninFragment : Fragment() {

    private lateinit var binding: SigninFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SigninFragmentBinding.inflate(inflater, container, false)
        binding.hdl = this
        return binding.root
    }

    fun performSignin(view: View) {
        // TODO - it's a placeholder for now
        startActivity(Intent(context, MainActivity::class.java)).apply {
            activity?.finish()
        }
    }

}