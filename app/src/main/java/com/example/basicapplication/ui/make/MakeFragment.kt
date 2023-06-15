package com.example.basicapplication.ui.make


import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.basicapplication.databinding.FragmentMakeBinding
import com.example.basicapplication.util.BaseFragment


class MakeFragment : BaseFragment<FragmentMakeBinding, MakeViewModel>() {


    override lateinit var binding: FragmentMakeBinding
    override val viewModel: MakeViewModel by viewModels()


    override fun getViewBinding(inflater: LayoutInflater): FragmentMakeBinding {
        return FragmentMakeBinding.inflate(inflater)
    }

}