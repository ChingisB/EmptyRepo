package com.example.basicapplication.ui.home


import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.setText(when (pos) {0 -> R.string.new_text else -> R.string.popular_text })
        }.attach()
        binding.searchBar.isEndIconVisible = false
    }

    override fun setupListeners() {

        binding.searchBarEditText.setOnEditorActionListener { view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){ searchBarCallbacks.forEach { it.invoke(view.text.toString()) }.let { true } }
            true
        }

        binding.searchBarEditText.addTextChangedListener { binding.searchBar.isEndIconVisible = true }

        binding.searchBar.setEndIconOnClickListener { view ->
            if(binding.searchBarEditText.text == null || binding.searchBarEditText.text.toString() == ""){
                view.isVisible = false
                binding.searchBarEditText.isCursorVisible = false
                searchBarClosedCallbacks.forEach { it.invoke() }
            }
            else binding.searchBarEditText.text = null
        }
    }

    fun addSearchBarCallback(callback: (query: String?) -> Unit) = searchBarCallbacks.add(callback)

    fun addSearchBarClosedCallback(callback: () -> Unit) = searchBarClosedCallbacks.add(callback)

}