package com.example.basicapplication.ui.welcome


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.TokenViewModel
import com.example.basicapplication.databinding.FragmentWelcomeBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment


class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, TokenViewModel>() {

    override val viewModel: TokenViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding() = FragmentWelcomeBinding.inflate(layoutInflater)

    override fun setupListeners() {
        super.setupListeners()
        binding.navigateToSignin.setOnClickListener { navigateTo(SignInFragment()) }
        binding.navigateToSignup.setOnClickListener { navigateTo(SignUpFragment()) }
    }

    override fun observeData() {
        super.observeData()
        viewModel.tokenLiveData.observe(this) { token ->
            when (token) {
                null -> {}
                else ->{
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    navigateTo(BottomNavigationFragment())
                }
            }
        }
    }
    private fun navigateTo(fragment: Fragment){
        parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, fragment).commit()
    }
}