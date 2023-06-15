package com.example.basicapplication.ui.profile_settings

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentProfileSettingsBinding
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.basicapplication.util.BaseFragment
import javax.inject.Inject

class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ProfileSettingsViewModel.Factory

    override val viewModel: ProfileSettingsViewModel by viewModels { viewModelFactory }
    override lateinit var binding: FragmentProfileSettingsBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.apply {
            signOutButton.setOnClickListener {
                viewModel.signOut()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, WelcomeFragment()).commit()
            }
            cancel.setOnClickListener { parentFragmentManager.popBackStack() }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileSettingsBinding {
        return FragmentProfileSettingsBinding.inflate(inflater)
    }


}