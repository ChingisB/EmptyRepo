package com.example.basicapplication.ui.adapter


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.basicapplication.ui.new_photos.NewPhotosFragment
import com.example.basicapplication.ui.popular_photos.PopularPhotosFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> NewPhotosFragment()
        else -> PopularPhotosFragment()
    }
}