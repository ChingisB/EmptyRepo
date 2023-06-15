package com.example.basicapplication.ui.bottom_navigation


import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentBottomNavigationBinding
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.profile.ProfileFragment
import com.example.basicapplication.util.BaseFragment


class BottomNavigationFragment : BaseFragment<FragmentBottomNavigationBinding, BottomNavigationViewModel>() {

    override lateinit var binding: FragmentBottomNavigationBinding
    override val viewModel: BottomNavigationViewModel by viewModels()


    override fun getViewBinding(inflater: LayoutInflater): FragmentBottomNavigationBinding {
        return FragmentBottomNavigationBinding.inflate(inflater)
    }

    override fun setupListeners() {
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.navigation_make -> MakeFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            childFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, fragment).addToBackStack(null).commit()
            true
        }
    }
}