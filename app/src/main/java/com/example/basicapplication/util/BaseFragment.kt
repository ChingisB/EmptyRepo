package com.example.basicapplication.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VBinding: ViewBinding, ViewModel: BaseViewModel>: Fragment(){

    protected abstract val viewModel: ViewModel
    protected abstract var binding: VBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater)
        setupViews()
        setupListeners()
        addOnBackPressedCallbacks(requireActivity().onBackPressedDispatcher)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    protected open fun setupListeners() {}

    protected open fun setupViews() {}

    protected open fun observeData() {}

    protected open fun addOnBackPressedCallbacks(dispatcher: OnBackPressedDispatcher) {}

    protected abstract fun getViewBinding(inflater: LayoutInflater): VBinding

}