package com.example.basicapplication.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainActivity
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentProfileBinding
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import com.example.basicapplication.util.BaseFragment
import com.example.basicapplication.util.Constants
import javax.inject.Inject


class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    @Inject
    lateinit var viewModelFactory: ProfileViewModel.Factory

    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }
    override lateinit var binding: FragmentProfileBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.settingsButton.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileSettingsFragment())
                .addToBackStack(Constants.profileSettings).commit()
        }
    }

    override fun observeData() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.username.text = it.username
            binding.birthday.text = it.birthday
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater)
    }

}