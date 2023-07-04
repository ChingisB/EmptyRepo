package com.example.basicapplication.ui.home


import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentHomeBinding
import com.example.basicapplication.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    private val searchBarCallbacks = mutableListOf<(String?) -> Unit>()
    private val searchBarClosedCallbacks = mutableListOf<() -> Unit>()

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupViews() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setText(R.string.new_text)
                else -> tab.setText(R.string.popular_text)
            }
        }.attach()

        binding.searchBar.isClickable = true
    }

    override fun setupListeners() {
        binding.searchBar.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = searchBarCallbacks.forEach { it.invoke(query) }.let { true }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })

        binding.searchBar.setOnCloseListener {
            binding.searchBar.clearFocus()
            binding.searchBar.onActionViewCollapsed()
            searchBarClosedCallbacks.forEach{ it.invoke() }
            true
        }

        binding.searchBar.setOnClickListener { binding.searchBar.isIconified = false }
    }

    fun addSearchBarCallback(callback: (query: String?) -> Unit) = searchBarCallbacks.add(callback)

    fun addSearchBarClosedCallback(callback: () -> Unit) = searchBarClosedCallbacks.add(callback)

}