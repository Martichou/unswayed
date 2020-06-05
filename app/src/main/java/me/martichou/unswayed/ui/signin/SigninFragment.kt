package me.martichou.unswayed.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.R
import me.martichou.unswayed.databinding.SigninFragmentBinding
import timber.log.Timber

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

    fun gotoSignup(view: View) {
        Timber.d("Clicked")
        findNavController().navigate(R.id.signup_fragment)
    }

}