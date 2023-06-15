package com.example.basicapplication.ui.sign_up

import android.content.Context
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentSignUpBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.basicapplication.util.BaseFragment
import com.example.basicapplication.util.MaskWatcher
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {


    @Inject lateinit var viewModelFactory: SignUpViewModel.Factory

    override lateinit var binding: FragmentSignUpBinding
    override val viewModel by viewModels<SignUpViewModel> { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.apply {
            cancelButton.setOnClickListener {navigateTo(WelcomeFragment()) }

            signIn.setOnClickListener {navigateTo(SignInFragment()) }

            signUp.setOnClickListener { viewModel.onEvent(SignUpFormEvent.Submit) }

            username.addTextChangedListener { text -> viewModel.onEvent(SignUpFormEvent.UsernameChanged(text.toString())) }

            birthday.addTextChangedListener { text -> viewModel.onEvent(SignUpFormEvent.BirthdayChanged(text.toString())) }

            birthday.addTextChangedListener(MaskWatcher.buildDateMask())

            email.addTextChangedListener { text -> viewModel.onEvent(SignUpFormEvent.EmailChanged(text.toString())) }

            password.addTextChangedListener { text -> viewModel.onEvent(SignUpFormEvent.PasswordChanged(text.toString())) }

            confirmPassword.addTextChangedListener { text -> viewModel.onEvent(SignUpFormEvent.ConfirmPasswordChanged(text.toString())) }
        }
    }

    private fun navigateTo(fragment: Fragment) = parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()


    override fun addOnBackPressedCallbacks(dispatcher: OnBackPressedDispatcher){
        dispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, WelcomeFragment()).commit()
            }
        })
    }


    override fun setupViews() {
//            TODO move listeners
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentSignUpBinding {
       return FragmentSignUpBinding.inflate(inflater)
    }

    override fun observeData() {
        super.observeData()
        //TODO context
        val context = requireContext()
        viewModel.signedUp.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SignInFragment()).commit()
            }
        }
        viewModel.signUpFormState.observe(viewLifecycleOwner) { formState ->
            binding.usernameLayout.error = formState.usernameError?.asString(context)
            binding.birthdayLayout.error = formState.birthdayError?.asString(context)
            binding.emailLayout.error = formState.emailError?.asString(context)
            binding.passwordLayout.error = formState.passwordError?.asString(context)
            binding.confirmPasswordLayout.error = formState.confirmPasswordError?.asString(context)
        }
    }
}