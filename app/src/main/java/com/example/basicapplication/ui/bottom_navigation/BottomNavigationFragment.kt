package com.example.basicapplication.ui.bottom_navigation


import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentBottomNavigationBinding
import com.example.basicapplication.ui.favourite.FavouriteFragment
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.profile.ProfileFragment

class BottomNavigationFragment : BaseFragment<FragmentBottomNavigationBinding, BottomNavigationViewModel>() {

    override val viewModel: BottomNavigationViewModel by viewModels()

    override fun getViewBinding() = FragmentBottomNavigationBinding.inflate(layoutInflater)

    override fun setupListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.navigation_make -> MakeFragment()
                R.id.navigation_profile -> ProfileFragment()
                R.id.navigation_saved -> FavouriteFragment()
                else -> HomeFragment()
            }
            childFragmentManager.beginTransaction().replace(R.id.bottomNavigationFragmentContainer, fragment).commit()
            true
        }
    }
}
