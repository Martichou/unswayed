package me.martichou.unswayed.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.martichou.unswayed.databinding.SigninFragmentBinding
import me.martichou.unswayed.utils.isEmailValid

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

    fun gotoNext(view: View) {
        // TODO - will need to check more for that

        // Get the email value and validate it using a regex
        val email = binding.emailValue.text.toString()
        if (!isEmailValid(email)) {
            binding.emailValue.error = "Invalid email format"
            return
        }

        findNavController().navigate(SigninFragmentDirections.gotoNext(email))
    }

}