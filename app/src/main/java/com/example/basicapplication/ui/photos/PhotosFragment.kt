package com.example.basicapplication.ui.photos


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.basicapplication.*
import com.example.basicapplication.base.PhotoPagingFragment
import com.example.basicapplication.data.model.Photo
import com.example.basicapplication.databinding.FragmentNewPhotosBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.util.*
import javax.inject.Inject


class PhotosFragment : PhotoPagingFragment<FragmentNewPhotosBinding, PhotosViewModel>() {

    @Inject
    lateinit var viewModelFactory: PhotosViewModel.Factory
    override val viewModel by viewModels<PhotosViewModel> { viewModelFactory }
    override lateinit var listAdapter: PhotoListAdapter
    override val spanCount = 2
    override val spaceSize = 10
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            viewModel.new = it.getBoolean("new")
            viewModel.popular = it.getBoolean("popular")
        }
    }

    override fun createListAdapter() = PhotoListAdapter {
        sharedPhotoViewModel.setPhoto(it)
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .add(R.id.activityFragmentContainer, PhotoDetailsFragment()).addToBackStack(Constants.PHOTO_DETAILS).commit()
    }

    override fun setupViews() {
        super.setupViews()
        setupRecyclerView(binding.photoGrid)
    }

    override fun changePageLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    override fun showPageError(error: Resource.Error<List<Photo>>) {
        Log.e("some error", error.message.toString())
    }

    override fun getViewBinding() = FragmentNewPhotosBinding.inflate(layoutInflater)

}