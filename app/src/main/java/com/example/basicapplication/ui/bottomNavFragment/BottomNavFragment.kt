package com.example.basicapplication.ui.bottomNavFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentBottomNavBinding
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.make.MakeFragment
import com.example.basicapplication.ui.newPhotos.NewPhotosFragment
import com.example.basicapplication.ui.profile.ProfileFragment


class BottomNavFragment : Fragment() {

    lateinit var binding: FragmentBottomNavBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomNavBinding.inflate(inflater)

        setupBottomNavigation()

        return binding.root
    }


    private fun setupBottomNavigation() {
        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.navigation_home -> NewPhotosFragment()
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