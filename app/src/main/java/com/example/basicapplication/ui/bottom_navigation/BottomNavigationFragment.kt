package com.example.basicapplication.ui.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentBottomNavigationBinding
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.profile.ProfileFragment


class BottomNavigationFragment : Fragment() {

    lateinit var binding: FragmentBottomNavigationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomNavigationBinding.inflate(inflater)

        setupBottomNavigation()

        return binding.root
    }


    private fun setupBottomNavigation() {
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.navigation_make -> MakeFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragment()
            }


            childFragmentManager.beginTransaction().replace(R.id.fragmentContainerMain, fragment)
                .setReorderingAllowed(true).addToBackStack(null)
                .commit()

            true
        }
    }

}