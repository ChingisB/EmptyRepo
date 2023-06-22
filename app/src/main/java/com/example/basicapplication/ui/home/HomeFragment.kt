package com.example.basicapplication.ui.home


import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentHomeBinding
import com.example.basicapplication.ui.adapter.ViewPagerAdapter
import com.example.basicapplication.ui.photos.PhotosFragment
import com.example.basicapplication.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    private val searchBarCallbacks = mutableListOf<(String?) -> Unit>()

    override fun setupViews() {
        super.setupViews()
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setText(R.string.new_text)
                else -> tab.setText(R.string.popular_text)
            }
        }.attach()
    }

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

}