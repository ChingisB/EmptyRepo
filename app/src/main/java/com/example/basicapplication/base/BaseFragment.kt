package com.example.basicapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VBinding: ViewBinding, ViewModel: BaseViewModel>: Fragment(){

    protected abstract val viewModel: ViewModel
    protected val binding: VBinding by lazy { getViewBinding() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViews()
        observeData()
        setupListeners()
        addOnBackPressedCallbacks(requireActivity().onBackPressedDispatcher)
        return binding.root
    }

    protected open fun setupListeners() = Unit

    protected open fun setupViews() = Unit

    protected open fun observeData() = Unit

    protected open fun addOnBackPressedCallbacks(dispatcher: OnBackPressedDispatcher) = Unit

    protected abstract fun getViewBinding(): VBinding

}