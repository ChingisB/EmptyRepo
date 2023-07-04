package com.example.basicapplication.ui.photo_details


import android.content.Context
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.databinding.FragmentPhotoDetailsBinding
import com.example.data.api.Config
import com.example.domain.entity.PhotoEntity
import com.example.util.Resource
import javax.inject.Inject


class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding, PhotoDetailsViewModel>() {

    @Inject lateinit var viewModelFactory: PhotoDetailsViewModel.Factory
    override val viewModel: PhotoDetailsViewModel by viewModels { viewModelFactory }
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()
    private lateinit var photoEntity: PhotoEntity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding() = FragmentPhotoDetailsBinding.inflate(layoutInflater)

    override fun setupListeners() {
        super.setupListeners()
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.root.setOnClickListener{}
        binding.saveButton.setOnClickListener {
            if(photoEntity.isSaved) viewModel.removePhoto(photoEntity)
            else viewModel.savePhoto(photoEntity)
        }
    }

    override fun observeData() {
        super.observeData()
        sharedPhotoViewModel.photoLiveData.observe(viewLifecycleOwner) {
            photoEntity = it
            binding.photoName.text = it.name
            binding.description.text = it.description
            binding.postDate.text = it.dateCreate
            binding.saveButton.isChecked = it.isSaved
            Glide.with(binding.image).load(Config.MEDIA_URL + it.image.name).into(binding.image)
        }

        sharedPhotoViewModel.userLiveData.observe(viewLifecycleOwner){
            binding.usernameText.text = it.username
        }

        viewModel.photoSavedState.observe(viewLifecycleOwner){
            if(it is Resource.Success) binding.saveButton.isChecked = it.data
            if(it is Resource.Error) Toast.makeText(requireContext(), R.string.saving_error, Toast.LENGTH_SHORT).show()
        }
    }

}