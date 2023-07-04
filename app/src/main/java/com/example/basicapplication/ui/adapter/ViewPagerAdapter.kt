package com.example.basicapplication.ui.adapter


import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.basicapplication.ui.photos.PhotosFragment
import com.example.basicapplication.util.Constants

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment{
        val photosFragment = PhotosFragment()
        photosFragment.arguments = when(position){
            0 -> bundleOf(Pair(Constants.NEW_KEY, true), Pair(Constants.POPULAR_KEY, false))
            else -> bundleOf(Pair(Constants.NEW_KEY, false), Pair(Constants.POPULAR_KEY, true))
        }
        return photosFragment
    }

}