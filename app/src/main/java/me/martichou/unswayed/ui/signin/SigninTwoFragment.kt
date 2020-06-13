package me.martichou.unswayed.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.databinding.SigninFragmentBinding
import me.martichou.unswayed.databinding.SigninTwoFragmentBinding

class SigninTwoFragment : Fragment() {

    private lateinit var binding: SigninTwoFragmentBinding
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        email = arguments?.getString("email")
        if (email.isNullOrEmpty()) {
            findNavController().popBackStack()
        }
        binding = SigninTwoFragmentBinding.inflate(inflater, container, false).apply {
            hdl = this@SigninTwoFragment
            who.text = email
        }
        return binding.root
    }

    fun connect(view: View) {
        // TODO - it's a placeholder for now
        startActivity(Intent(context, MainActivity::class.java)).apply {
            activity?.finish()
        }
    }

}