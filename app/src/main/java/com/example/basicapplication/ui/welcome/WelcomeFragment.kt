package com.example.basicapplication.ui.welcome


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.TokenViewModel
import com.example.basicapplication.databinding.FragmentWelcomeBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.sign_up.SignUpFragment
import com.example.basicapplication.base.BaseFragment
import javax.inject.Inject


class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {

    @Inject
    lateinit var tokenViewModelFactory: TokenViewModel.Factory
    override val viewModel: WelcomeViewModel by viewModels()
    private val tokenViewModel by viewModels<TokenViewModel> { tokenViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.navigateToSignin.setOnClickListener { navigateTo(SignInFragment()) }
        binding.navigateToSignup.setOnClickListener { navigateTo(SignUpFragment()) }
    }

    override fun observeData() {
        super.observeData()
        tokenViewModel.tokenLiveData.observe(this) { token ->
            when (token) {
                null -> {}
                else ->{
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, BottomNavigationFragment()).commit()
                }
            }
        }
    }

    override fun getViewBinding() = FragmentWelcomeBinding.inflate(layoutInflater)

    private fun navigateTo(fragment: Fragment){
        parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, fragment).commit()
    }
}