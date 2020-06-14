package me.martichou.unswayed.ui.signin

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.martichou.unswayed.MainActivity
import me.martichou.unswayed.databinding.SigninTwoFragmentBinding
import me.martichou.unswayed.models.AccessToken
import me.martichou.unswayed.models.LoginData
import me.martichou.unswayed.network.ApiService
import me.martichou.unswayed.network.RetrofitBuilder
import me.martichou.unswayed.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class SigninTwoFragment : Fragment() {

    private lateinit var binding: SigninTwoFragmentBinding
    private lateinit var service: ApiService
    private lateinit var tokenManager: TokenManager

    private var email: String? = null
    var call: Call<AccessToken>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        service = RetrofitBuilder().createService(ApiService::class.java)
        tokenManager =
            TokenManager.getInstance(requireContext().getSharedPreferences("prefs", MODE_PRIVATE))
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

    fun connect(view: View) {
        // Send email and password (plain text over HTTPS) and await for the return
        // Send using post with payload as {email: value, password: value}
        if (binding.passwordValue.text.isNullOrEmpty()) {
            binding.passwordValue.error = "Cannot be blank"
            return
        }
        call = service.auth(LoginData(email!!, binding.passwordValue.text.toString()))
        // Make the call
        call!!.enqueue(object : Callback<AccessToken> {
            override fun onResponse(
                call: Call<AccessToken>,
                response: Response<AccessToken>
            ) {
                if (response.isSuccessful) {
                    // Save the response (token_type, access_token, refresh_token, expire_at) somewhere safe
                    response.body()?.let {
                        tokenManager.saveToken(it)
                    }
                    // Continue
                    startActivity(Intent(context, MainActivity::class.java)).apply {
                        activity?.finish()
                    }
                } else {
                    if (response.code() == 401) {
                        binding.passwordValue.error = "Wrong password"
                    }
                }
            }

            override fun onFailure(
                call: Call<AccessToken?>?,
                t: Throwable
            ) {
                Timber.e(t)
            }
        })
    }

}