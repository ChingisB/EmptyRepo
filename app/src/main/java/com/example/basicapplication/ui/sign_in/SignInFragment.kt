package com.example.basicapplication.ui.sign_in

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.basicapplication.MainActivity
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentSignInBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.util.Resource
import javax.inject.Inject

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>() {

    @Inject
    lateinit var viewModelFactory: SignInViewModel.Factory
    override val viewModel by viewModels<SignInViewModel> { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding() = FragmentSignInBinding.inflate(layoutInflater)

    override fun setupListeners() {
        super.setupListeners()
        binding.apply {
            cancelButton.setOnClickListener {navigateTo(WelcomeFragment()) }
            signUpButton.setOnClickListener {navigateTo(SignUpFragment()) }
            signInButton.setOnClickListener { viewModel.submitEvent(SignInFormEvent.Submit) }

            email.addTextChangedListener { text -> viewModel.submitEvent(SignInFormEvent.EmailChanged(text.toString())) }
            signInPassword.addTextChangedListener { text -> viewModel.submitEvent(SignInFormEvent.PasswordChanged(text.toString())) }
        }
    }

    override fun addOnBackPressedCallbacks(dispatcher: OnBackPressedDispatcher){
        dispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, WelcomeFragment()).commit()
            }
        })
    }

    override fun observeData() {
        super.observeData()
        viewModel.signInFormState.observe(viewLifecycleOwner) {formState ->
            formState.emailError?.let { binding.emailLayout.error = it }
            formState.passwordError?.let { binding.signInPasswordLayout.error = it }
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.activityFragmentContainer, BottomNavigationFragment()).commit()
            }
        }
    }

    private fun navigateTo(fragment: Fragment) =
        parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, fragment).commit()

}