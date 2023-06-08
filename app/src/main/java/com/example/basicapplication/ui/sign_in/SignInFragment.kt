package com.example.basicapplication.ui.sign_in

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainActivity
import com.example.basicapplication.R
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentSignInBinding
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.basicapplication.util.Resource
import javax.inject.Inject


class SignInFragment : Fragment() {


    private lateinit var binding: FragmentSignInBinding

    @Inject
    lateinit var viewModelFactory: SignInViewModel.Factory

    private val viewModel by viewModels<SignInViewModel> { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as Context).appComponent.injectSignInFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInBinding.inflate(inflater)

        val context = (activity as Context)


        binding.apply {

            cancelButton.setOnClickListener { parentFragmentManager.clearBackStack("welcome") }


            cancelButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WelcomeFragment()).commit()
            }

            signUpButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SignUpFragment()).commit()
            }

            signInButton.setOnClickListener {
                viewModel.onEvent(SignInFormEvent.Submit)
            }

            email.addTextChangedListener { text ->
                viewModel.onEvent(SignInFormEvent.EmailChanged(text.toString()))
            }

            signInPassword.addTextChangedListener { text ->
                viewModel.onEvent(SignInFormEvent.PasswordChanged(text.toString()))
            }

            viewModel.signInFormState.observe(viewLifecycleOwner) {
                emailLayout.error = it.emailError?.asString(context)
                signInPasswordLayout.error = it.passwordError?.asString(context)
            }

            viewModel.loggedIn.observe(viewLifecycleOwner) {
                if (it is Resource.Success) {
                    (activity as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment()).commit()
                }
            }

        }

        return binding.root
    }


}