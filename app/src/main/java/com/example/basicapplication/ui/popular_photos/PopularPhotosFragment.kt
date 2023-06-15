package com.example.basicapplication.ui.popular_photos

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.databinding.FragmentPopularPhotosBinding
import com.example.basicapplication.util.BaseFragment
import javax.inject.Inject


class PopularPhotosFragment : BaseFragment<FragmentPopularPhotosBinding, PopularPhotosViewModel>() {

    @Inject
    lateinit var viewModelFactory: PopularPhotosViewModel.Factory

    override val viewModel: PopularPhotosViewModel by viewModels { viewModelFactory }
    override lateinit var binding: FragmentPopularPhotosBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentPopularPhotosBinding {
        return FragmentPopularPhotosBinding.inflate(inflater)
    }
}