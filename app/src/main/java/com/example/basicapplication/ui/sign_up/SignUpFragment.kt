package com.example.basicapplication.ui.sign_up

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentSignUpBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding


    @Inject
    lateinit var viewModelFactory: SignUpViewModel.Factory


    private val viewModel by viewModels<SignUpViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Context).appComponent.injectSignUpFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)


        viewModel.signedUp.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SignInFragment()).commit()
            }
        }

        val context = (activity as Context)



        binding.apply {
            viewModel.signUpFormState.observe(viewLifecycleOwner) { formState ->
                usernameLayout.error = formState.usernameError?.asString(context)
                birthdayLayout.error = formState.birthdayError?.asString(context)
                emailLayout.error = formState.emailError?.asString(context)
                passwordLayout.error = formState.passwordError?.asString(context)
                confirmPasswordLayout.error = formState.confirmPasswordError?.asString(context)
            }

            cancelButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WelcomeFragment()).commit()
            }


            signIn.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SignInFragment()).commit()
            }

            signUp.setOnClickListener {
                viewModel.onEvent(SignUpFormEvent.Submit)
            }


            username.addTextChangedListener { text ->
                viewModel.onEvent(
                    SignUpFormEvent.UsernameChanged(
                        text.toString()
                    )
                )
            }
            birthday.addTextChangedListener { text ->
                viewModel.onEvent(
                    SignUpFormEvent.BirthdayChanged(
                        text.toString()
                    )
                )
            }
            email.addTextChangedListener { text ->
                viewModel.onEvent(
                    SignUpFormEvent.EmailChanged(
                        text.toString()
                    )
                )
            }
            password.addTextChangedListener { text ->
                viewModel.onEvent(
                    SignUpFormEvent.PasswordChanged(
                        text.toString()
                    )
                )
            }
            confirmPassword.addTextChangedListener { text ->
                viewModel.onEvent(
                    SignUpFormEvent.ConfirmPasswordChanged(
                        text.toString()
                    )
                )
            }
        }

        return binding.root
    }

}