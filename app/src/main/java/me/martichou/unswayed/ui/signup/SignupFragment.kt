package me.martichou.unswayed.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.signup_fragment.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    fun performSignup(view: View) {
        // TODO - it's a placeholder for now
        startActivity(Intent(context, MainActivity::class.java)).apply {
            activity?.finish()
        }
    }

    private fun subscribeUi() {
        password_value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    progressBar.progress = 0
                } else {
                    progressBar.progress = (s.length.toFloat() / 16 * 100).toInt()
                }
            }
        })
    }

}