package me.martichou.unswayed.ui.signin

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.databinding.SigninTwoFragmentBinding
import me.martichou.unswayed.models.LoginData
import me.martichou.unswayed.network.RetrofitBuilder
import me.martichou.unswayed.utils.Status
import me.martichou.unswayed.utils.TokenManager

class SigninTwoFragment : Fragment() {

    private lateinit var binding: SigninTwoFragmentBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var tokenManager: TokenManager

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        tokenManager = TokenManager.getInstance(requireContext().getSharedPreferences("prefs", MODE_PRIVATE))
        super.onCreate(savedInstanceState)
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, AuthVMFactory(RetrofitBuilder.authService)).get(AuthViewModel::class.java)
    }

    fun View.connect() {
        if (binding.passwordValue.text.isNullOrEmpty()) {
            binding.passwordValue.error = "Cannot be blank"
            return
        }
        viewModel.perform(LoginData(email!!, binding.passwordValue.text.toString())).observe(this@SigninTwoFragment, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { accessToken ->
                            tokenManager.saveToken(accessToken)
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(context, MainActivity::class.java)).apply {
                                activity?.finish()
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}