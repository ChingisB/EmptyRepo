package com.example.basicapplication.ui.bottom_navigation


import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentBottomNavigationBinding
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.profile.ProfileFragment
import com.example.basicapplication.base.BaseFragment


class BottomNavigationFragment : BaseFragment<FragmentBottomNavigationBinding, BottomNavigationViewModel>() {

    override val viewModel: BottomNavigationViewModel by viewModels()

    override fun getViewBinding() = FragmentBottomNavigationBinding.inflate(layoutInflater)

    override fun setupListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_make -> {
                    childFragmentManager.beginTransaction().replace(R.id.bottomNavigationFragmentContainer, MakeFragment()).commit()
                }
                R.id.navigation_profile -> {
                    childFragmentManager.beginTransaction().replace(R.id.bottomNavigationFragmentContainer, ProfileFragment())
                        .addToBackStack(null).commit()
                }
                else -> {
                    childFragmentManager.beginTransaction().replace(R.id.bottomNavigationFragmentContainer, HomeFragment())
                    .addToBackStack(null).commit()
                }
            }

            true
        }
    }

}