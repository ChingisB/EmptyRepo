package com.example.basicapplication.ui.photo_details


import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.databinding.FragmentPhotoDetailsBinding
import com.example.basicapplication.util.BaseFragment


class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding, SharedPhotoViewModel>() {

    override lateinit var binding: FragmentPhotoDetailsBinding
    override val viewModel: SharedPhotoViewModel by activityViewModels()


    override fun setupListeners() {
        super.setupListeners()
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.photoName.text = it.name
            binding.description.text = it.description
            binding.postDate.text = it.dateCreate
            Glide.with(binding.image).load(Config.mediaUrl + it.image?.name).into(binding.image)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentPhotoDetailsBinding {
        return FragmentPhotoDetailsBinding.inflate(inflater)
    }

}