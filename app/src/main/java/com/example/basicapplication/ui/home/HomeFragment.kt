package com.example.basicapplication.ui.home


import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.ui.adapter.ViewPagerAdapter
import com.example.basicapplication.databinding.FragmentHomeBinding
import com.example.basicapplication.util.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override lateinit var binding: FragmentHomeBinding
    override val viewModel: HomeViewModel by viewModels()


    override fun setupListeners() {
        super.setupListeners()
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setText(R.string.new_text)
                else -> tab.setText(R.string.popular_text)
            }
        }.attach()
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

}