package com.example.basicapplication.ui.sign_in

import android.content.Context
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainActivity
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentSignInBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.basicapplication.util.BaseFragment
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>() {

    @Inject
    lateinit var viewModelFactory: SignInViewModel.Factory

    override lateinit var binding: FragmentSignInBinding
    override val viewModel by viewModels<SignInViewModel> { viewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.apply {

            cancelButton.setOnClickListener {navigateTo(WelcomeFragment()) }

            signUpButton.setOnClickListener {navigateTo(SignUpFragment()) }

            signInButton.setOnClickListener { viewModel.onEvent(SignInFormEvent.Submit) }

            email.addTextChangedListener { text -> viewModel.onEvent(SignInFormEvent.EmailChanged(text.toString())) }

            signInPassword.addTextChangedListener { text -> viewModel.onEvent(SignInFormEvent.PasswordChanged(text.toString())) }
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

    override fun getViewBinding(inflater: LayoutInflater): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater)
    }

    override fun observeData() {
        super.observeData()
//        TODO requireContext
        val context = requireContext()
        viewModel.signInFormState.observe(viewLifecycleOwner) {
            binding.emailLayout.error = it.emailError?.asString(context)
            binding.signInPasswordLayout.error = it.passwordError?.asString(context)
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BottomNavigationFragment()).commit()
            }
        }
    }

}