package me.martichou.unswayedphotos.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.data.model.api.CredentialsData
import me.martichou.unswayedphotos.databinding.PasswordFragmentBinding
import me.martichou.unswayedphotos.di.Injectable
import me.martichou.unswayedphotos.di.injectViewModel
import me.martichou.unswayedphotos.ui.MainActivity
import me.martichou.unswayedphotos.util.TokenManager
import javax.inject.Inject

class PasswordFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: PasswordFragmentBinding
    private lateinit var viewModel: PasswordViewModel

    private val args: PasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (args.email.isEmpty())
            findNavController().popBackStack()
        viewModel = injectViewModel(viewModelFactory)
        binding = PasswordFragmentBinding.inflate(inflater, container, false).apply {
            hdl = this@PasswordFragment
            who.text = args.email
        }

        return binding.root
    }

    fun View.processConnection() {
        if (binding.passwordValue.text.isNullOrEmpty()) {
            binding.passwordValue.error = "This field cannot be empty"
            return
        }
        viewModel.processConnection(
            CredentialsData(
                args.email,
                binding.passwordValue.text.toString()
            )
        ).observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data == null) {
                        Snackbar.make(binding.root, "Try again later...", Snackbar.LENGTH_LONG)
                            .show()
                        return@Observer
                    }
                    tokenManager.saveToken(result.data)
                    sharedPreferences.edit().putString("user_email", args.email).apply()
                    startActivity(Intent(context, MainActivity::class.java))
                        .apply { activity?.finish() }
                }
                Result.Status.LOADING -> {
                    Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_LONG).show()
                }
                Result.Status.ERROR -> {
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}