package com.example.basicapplication.ui.welcome


import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentWelcomeBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.util.BaseFragment


class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {

    override lateinit var binding: FragmentWelcomeBinding
    override val viewModel: WelcomeViewModel by viewModels()


    override fun setupListeners() {
        super.setupListeners()
        binding.navigateToSignin.setOnClickListener { navigateTo(SignInFragment()) }

        binding.navigateToSignup.setOnClickListener { navigateTo(SignUpFragment()) }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater)
    }

    private fun navigateTo(fragment: Fragment){
        parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}