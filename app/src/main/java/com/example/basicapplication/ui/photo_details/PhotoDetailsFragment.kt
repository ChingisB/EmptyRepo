package com.example.basicapplication.ui.photo_details


import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.databinding.FragmentPhotoDetailsBinding
import com.example.basicapplication.base.BaseFragment


class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding, SharedPhotoViewModel>() {

    override val viewModel: SharedPhotoViewModel by activityViewModels()

    override fun setupListeners() {
        super.setupListeners()
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.rootLayout.setOnClickListener{}
    }

    override fun observeData() {
        super.observeData()
        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.photoName.text = it.name
            binding.description.text = it.description
            binding.postDate.text = it.dateCreate
            Glide.with(binding.image).load(Config.MEDIA_URL + it.image?.name).into(binding.image)
        }
    }

    override fun getViewBinding() = FragmentPhotoDetailsBinding.inflate(layoutInflater)

}