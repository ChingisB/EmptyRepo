package com.example.basicapplication.ui.sign_in

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentSignInBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
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

    override fun setupListeners() = with(binding) {
        cancelButton.setOnClickListener { navigateTo(WelcomeFragment()) }
        signUpButton.setOnClickListener { navigateTo(SignUpFragment()) }
        signInButton.setOnClickListener { viewModel.submitEvent(SignInFormEvent.Submit) }

        email.addTextChangedListener { text -> viewModel.submitEvent(SignInFormEvent.EmailChanged(text.toString())) }
        signInPassword.addTextChangedListener { text -> viewModel.submitEvent(SignInFormEvent.PasswordChanged(text.toString())) }
    }.let {}

    override fun addOnBackPressedCallbacks(dispatcher: OnBackPressedDispatcher) {
        dispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) { override fun handleOnBackPressed() { navigateTo(WelcomeFragment()) } }
        )
    }

    override fun observeData() {
        super.observeData()
        viewModel.signInFormState.observe(viewLifecycleOwner) { formState ->
            binding.emailLayout.error = formState.emailError
            binding.signInPasswordLayout.error = formState.passwordError
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) { if (it) navigateTo(BottomNavigationFragment()) }
    }

    private fun navigateTo(fragment: Fragment) =
        parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, fragment).commit()

}