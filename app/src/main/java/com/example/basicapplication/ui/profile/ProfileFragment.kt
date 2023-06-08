package com.example.basicapplication.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainActivity
import com.example.basicapplication.R
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentProfileBinding
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import javax.inject.Inject


class ProfileFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ProfileViewModel.Factory


    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }


    lateinit var binding: FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as Context).appComponent.injectProfileFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewModel.getUser()

        binding = FragmentProfileBinding.inflate(inflater)

        binding.settingsButton.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileSettingsFragment())
                .addToBackStack("profileSettings").commit()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.username.text = it.username
            binding.birthday.text = it.birthday
        }

        return binding.root
    }

}