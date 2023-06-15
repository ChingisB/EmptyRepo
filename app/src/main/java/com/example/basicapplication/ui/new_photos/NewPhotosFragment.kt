package com.example.basicapplication.ui.new_photos


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.basicapplication.*
import com.example.basicapplication.databinding.FragmentNewPhotosBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.util.BaseFragment
import com.example.basicapplication.util.Constants
import com.example.basicapplication.util.RecyclerViewPaginator
import com.example.basicapplication.util.Resource
import javax.inject.Inject
import dagger.Lazy


class NewPhotosFragment : BaseFragment<FragmentNewPhotosBinding, NewPhotosViewModel>() {

    @Inject
    lateinit var viewModelFactory: Lazy<NewPhotosViewModel.Factory>

    override lateinit var binding: FragmentNewPhotosBinding
    override val viewModel by viewModels<NewPhotosViewModel> { viewModelFactory.get() }

    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()
    private lateinit var photoListAdapter: PhotoListAdapter
    private lateinit var paginator: RecyclerViewPaginator


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    private fun getPhotoListAdapter(): PhotoListAdapter{
        return PhotoListAdapter(emptyList()) {
            sharedPhotoViewModel.setPhoto(it)
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .addToBackStack(Constants.photoDetails).add(R.id.fragmentContainer, PhotoDetailsFragment()).commit()
        }
    }

    private fun getPaginator(): RecyclerViewPaginator{
        return object : RecyclerViewPaginator() {

            override var isLastPage: Boolean = false

            override var isLoading: Boolean = false

            override fun loadMore(start: Int) { viewModel.fetch(start) }
        }
    }

    override fun setupViews() {
        photoListAdapter = getPhotoListAdapter()
        paginator = getPaginator()
        binding.photoGrid.apply {
            adapter = photoListAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(paginator)
        }
    }

    override fun observeData() {
        viewModel.isLastPage.observe(viewLifecycleOwner) {
            paginator.isLastPage = it
        }

        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { it1 -> photoListAdapter.submitList(it1) }
                    binding.progressBar.visibility = View.GONE
                    paginator.isLoading = false
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    paginator.isLoading = true
                }
                is Resource.Error -> { Log.e(Constants.networkError, it.message.toString()) }
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentNewPhotosBinding {
        return FragmentNewPhotosBinding.inflate(inflater)
    }

}