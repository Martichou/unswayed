package me.martichou.unswayedphotos.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import me.martichou.unswayedphotos.databinding.EmailFragmentBinding
import me.martichou.unswayedphotos.util.isaValidEmail

class EmailFragment : Fragment() {

    private lateinit var binding: EmailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EmailFragmentBinding.inflate(inflater, container, false).apply {
            hdl = this@EmailFragment
        }

        return binding.root
    }

    fun View.gotoPwd() {
        val email = binding.emailValue.text.toString()
        if (email.isEmpty() || !email.isaValidEmail()) {
            binding.emailTextInput.error = "Invalid Email"
            return
        }
        findNavController().navigate(EmailFragmentDirections.gotoNext(email))
    }

}