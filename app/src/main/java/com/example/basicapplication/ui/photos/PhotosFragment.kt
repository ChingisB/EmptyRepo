package com.example.basicapplication.ui.photos


import android.content.Context
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.PagingFragment
import com.example.basicapplication.*
import com.example.basicapplication.dagger.DaggerViewModelFactory
import com.example.basicapplication.databinding.FragmentNewPhotosBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.home.HomeFragment
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.util.*
import com.example.domain.entity.PaginatedPhotosEntity
import com.example.util.PlaceHolderAdapter
import com.example.util.Resource
import javax.inject.Inject


class PhotosFragment : PagingFragment<FragmentNewPhotosBinding, PaginatedPhotosEntity, PhotosViewModel, PhotoListAdapter>() {

    @Inject lateinit var viewModelFactory: DaggerViewModelFactory
    override val viewModel: PhotosViewModel by viewModels{ viewModelFactory }
    override val spanCount = 2
    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels { viewModelFactory}


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.new = it.getBoolean(Constants.NEW_KEY)
            viewModel.popular = it.getBoolean(Constants.POPULAR_KEY)
        }
        viewModel.loadPage()
    }

    override fun setupViews() = setupRecyclerView(binding.photoGrid)

    override fun getViewBinding() = FragmentNewPhotosBinding.inflate(layoutInflater)

    override fun createListAdapter() = PhotoListAdapter {
        sharedPhotoViewModel.setPhoto(it)
        requireActivity().supportFragmentManager.commit {
            add(R.id.activityFragmentContainer, PhotoDetailsFragment())
            addToBackStack(Constants.PHOTO_DETAILS)
        }
    }

    override fun setupListeners() {
        binding.refreshBar.setOnRefreshListener(viewModel::refreshData)
        (parentFragment as HomeFragment).addSearchBarCallback { viewModel.refreshDataWithNewQuery(it ?: "") }
        (parentFragment as HomeFragment).addSearchBarClosedCallback { viewModel.refreshDataWithNewQuery("") }
    }

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