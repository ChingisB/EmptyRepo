package com.example.basicapplication.ui.adapter


import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.basicapplication.ui.photos.PhotosFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment{
        val photosFragment = PhotosFragment()
        if(position == 0) photosFragment.arguments = bundleOf(Pair("new", true), Pair("popular", false))
        else photosFragment.arguments = bundleOf(Pair("new", false), Pair("popular", true))
        return photosFragment
    }

}