package com.example.basicapplication.ui.profile

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.base.PagingFragment
import com.example.basicapplication.MainActivity
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.SharedUserViewModel
import com.example.basicapplication.databinding.FragmentProfileBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.util.PlaceHolderAdapter
import com.example.util.Resource
import javax.inject.Inject

class ProfileFragment : PagingFragment<FragmentProfileBinding, PaginatedPhotosEntity, ProfileViewModel, PhotoListAdapter>() {

    @Inject lateinit var viewModelFactory: ProfileViewModel.Factory
    @Inject lateinit var sharedPhotoViewModelFactory: SharedPhotoViewModel.Factory
    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }
    override val spanCount = 4
    override val spaceSize = 6
    private val sharedUserViewModel: SharedUserViewModel by activityViewModels()
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels { sharedPhotoViewModelFactory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun createListAdapter(): PhotoListAdapter {
        return PhotoListAdapter {
            sharedPhotoViewModel.setPhoto(it)
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(Constants.PHOTO_DETAILS).add(R.id.activityFragmentContainer, PhotoDetailsFragment()).commit()
        }
    }

    override fun setupViews() {
        setupRecyclerView(binding.photoGrid)
        changePageLoadingState(false)
    }

    override fun setupListeners() = binding.refreshBar.setOnRefreshListener { viewModel.refreshData() }

    override fun observeData() {
        super.observeData()
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> it.data.let { value ->
                    sharedUserViewModel.setUser(value)
                    viewModel.userId = value.id
                    if (viewModel.data.value == null) viewModel.loadPage()
                    if (viewModel.avatarLiveData.value == null) viewModel.getAvatar()
                    binding.settingsButton.setOnClickListener {
                        (requireActivity()).supportFragmentManager.beginTransaction()
                            .add(R.id.activityFragmentContainer, ProfileSettingsFragment())
                            .addToBackStack(Constants.PROFILE_SETTINGS)
                            .commit()
                    }
                    viewModel.getUserViews(value) { totalViews ->
                        binding.totalViewsText.text = if(totalViews > 999) "999+" else totalViews.toString()
                    }
                }

                is Resource.Loading -> {}
                is Resource.Error -> { Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show() }
            }
        }

        viewModel.data.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success ->{
                    binding.totalImagesText.text = it.data.totalItems.toString()
                    binding.refreshBar.isRefreshing = false
                }
                is Resource.Loading -> if (binding.refreshBar.isRefreshing) binding.progressBar.isVisible = false
                else -> binding.refreshBar.isRefreshing = false
            }
        }

        viewModel.avatarLiveData.observe(viewLifecycleOwner){ if(it is Resource.Success) sharedUserViewModel.setAvatar(it.data) }

        sharedUserViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.username.text = it.username
            binding.birthday.text = it.birthday
        }

        sharedUserViewModel.avatarLiveData.observe(viewLifecycleOwner){
            Glide.with(this).load(it).into(binding.avatarImage)
            binding.avatarImage.scaleType = ImageView.ScaleType.CENTER
        }
    }

    override fun changePageLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun showPageError(error: Resource.Error) {
        binding.photoGrid.adapter = PlaceHolderAdapter()
        binding.photoGrid.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun showPage() {
        binding.photoGrid.adapter = listAdapter
        binding.photoGrid.layoutManager = createLayoutManager()
    }


}