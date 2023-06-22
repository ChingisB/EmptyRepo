package com.example.basicapplication.ui.profile

import android.content.Context
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.basicapplication.*
import com.example.basicapplication.base.PhotoPagingFragment
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.databinding.FragmentProfileBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.ui.profile_settings.ProfileSettingsFragment
import com.example.basicapplication.util.*
import javax.inject.Inject


class ProfileFragment : PhotoPagingFragment<FragmentProfileBinding, ProfileViewModel>() {

    @Inject
    lateinit var viewModelFactory: ProfileViewModel.Factory
    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }
    override lateinit var listAdapter: PhotoListAdapter
    override val spanCount = 4
    override val spaceSize = 6
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()
    private val sharedUserViewModel: SharedUserViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun createListAdapter(): PhotoListAdapter {
        return PhotoListAdapter {
            sharedPhotoViewModel.setPhoto(it)
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(Constants.PHOTO_DETAILS).add(R.id.activityFragmentContainer, PhotoDetailsFragment()).commit()
        }
    }
    override fun observeData() {
        super.observeData()
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success<*> -> it.data?.let { value ->
                    sharedUserViewModel.setUser(value)
                    viewModel.userId = value.id
                    if(viewModel.data.value == null){
                        viewModel.loadPage()
                    }
                    binding.settingsButton.setOnClickListener {
                        (activity as MainActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.activityFragmentContainer, ProfileSettingsFragment())
                            .addToBackStack(Constants.PROFILE_SETTINGS).commit()
                    }
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
        sharedUserViewModel.userLiveData.observe(viewLifecycleOwner){
            binding.username.text = it.username
            binding.birthday.text = it.birthday
        }
    }

    override fun setupViews() {
        super.setupViews()
        setupRecyclerView(binding.photoGrid)
    }

    override fun changePageLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun showPageError(error: Resource.Error<List<Photo>>) {
    }

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

}