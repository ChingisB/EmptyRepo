package com.example.basicapplication.ui.favourite

import android.content.Context
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.PagingFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.databinding.FragmentFavouriteBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.util.Constants
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.util.PlaceHolderAdapter
import com.example.util.Resource
import javax.inject.Inject

class FavouriteFragment : PagingFragment<FragmentFavouriteBinding, PaginatedPhotosEntity, FavouriteViewModel, PhotoListAdapter>() {

    @Inject lateinit var viewModelFactory: FavouriteViewModel.Factory
    override val spanCount: Int = 2
    override val viewModel: FavouriteViewModel by viewModels { viewModelFactory }
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun getViewBinding() = FragmentFavouriteBinding.inflate(layoutInflater)

    override fun setupViews() = setupRecyclerView(binding.photoGrid)

    override fun createListAdapter(): PhotoListAdapter =
        PhotoListAdapter {
            sharedPhotoViewModel.setPhoto(it)
            (requireActivity()).supportFragmentManager
                .beginTransaction()
                .addToBackStack(Constants.PHOTO_DETAILS)
                .add(R.id.activityFragmentContainer, PhotoDetailsFragment())
                .commit()
        }

    override fun setupListeners() = binding.refreshBar.setOnRefreshListener { viewModel.refreshData() }

    override fun observeData() {
        super.observeData()
        viewModel.data.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> if(binding.refreshBar.isRefreshing) binding.progressBar.isVisible = false
                else -> binding.refreshBar.isRefreshing = false
            }
        }
    }

    override fun changePageLoadingState(isLoading: Boolean) { binding.progressBar.isVisible = isLoading }

    override fun showPageError(error: Resource.Error) {
        binding.photoGrid.adapter = PlaceHolderAdapter()
        binding.photoGrid.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun showPage() {
        binding.photoGrid.adapter = listAdapter
        binding.photoGrid.layoutManager = createLayoutManager()
    }

}