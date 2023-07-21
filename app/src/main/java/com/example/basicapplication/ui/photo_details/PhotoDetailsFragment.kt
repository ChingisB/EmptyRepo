package com.example.basicapplication.ui.photo_details


import android.animation.ValueAnimator
import android.content.Context
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.dagger.DaggerViewModelFactory
import com.example.basicapplication.databinding.FragmentPhotoDetailsBinding
import com.example.basicapplication.util.Constants
import com.example.data.api.Config
import com.example.domain.entity.PhotoEntity
import javax.inject.Inject


class PhotoDetailsFragment : BaseFragment<FragmentPhotoDetailsBinding, PhotoDetailsViewModel>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory
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
        binding.root.setOnClickListener {}
        binding.saveButton.setOnClickListener {
            val greyColor = requireContext().getColor(R.color.grey)
            val pinkColor = requireContext().getColor(R.color.dark_pink)
            if (photoEntity.isSaved) {
                viewModel.removePhoto(photoEntity)
                ValueAnimator.ofArgb(greyColor, pinkColor)
            } else {
                viewModel.savePhoto(photoEntity)
                ValueAnimator.ofArgb(pinkColor, greyColor)
            }.apply {
                duration = LIKE_ANIMATION_DURATION
                start()
                addUpdateListener { binding.saveButton.background.setTint(it.getAnimatedValue() as Int)}
            }
        }
    }

    override fun observeData() {
        sharedPhotoViewModel.photoLiveData.observe(viewLifecycleOwner) {
            photoEntity = it
            binding.photoName.text = it.name
            binding.description.text = it.description
            binding.postDate.text = it.dateCreate
            binding.saveButton.isChecked = it.isSaved
            Glide.with(binding.image).load(Config.MEDIA_URL + it.image.name).into(binding.image)
            viewModel.viewPhoto(it)
            viewModel.getTotalViews(it.id) { totalViews ->
                binding.totalViewsText.text = if (totalViews > 999) Constants.MAX_VIEWS else totalViews.toString()
            }
        }

        sharedPhotoViewModel.userLiveData.observe(viewLifecycleOwner) { binding.usernameText.text = it.username }

        viewModel.photoSavedState.observe(viewLifecycleOwner) { binding.saveButton.isChecked = it }
    }

    companion object{
        const val LIKE_ANIMATION_DURATION = 200L
    }

}