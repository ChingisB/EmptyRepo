package com.example.basicapplication.ui.new_photos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.basicapplication.MainActivity
import com.example.basicapplication.R
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentNewPhotosBinding
import com.example.basicapplication.ui.adapter.PhotoListAdapter
import com.example.basicapplication.ui.photo_details.PhotoDetailsFragment
import com.example.basicapplication.util.RecyclerViewPaginator
import javax.inject.Inject


class NewPhotosFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NewPhotosViewModel.Factory


    private val viewModel by viewModels<NewPhotosViewModel> { viewModelFactory }

    private val sharedPhotoViewModel: SharedPhotoViewModel by activityViewModels()


    private lateinit var photoListAdapter: PhotoListAdapter


    private lateinit var binding: FragmentNewPhotosBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Context).appComponent.injectNewPhotosFragment(this)
        viewModel.fetch(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentNewPhotosBinding.inflate(inflater)

        photoListAdapter = PhotoListAdapter(emptyList()) {
            sharedPhotoViewModel.setPhoto(it)
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .addToBackStack("photoDetails").replace(
                    R.id.fragmentContainer, PhotoDetailsFragment()
                ).commit()
        }



        binding.photoGrid.apply {
            adapter = photoListAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }

        val scrollListener = object : RecyclerViewPaginator(binding.photoGrid) {

            override var isLastPage: Boolean = false

            override fun loadMore(start: Int) {
                viewModel.fetch(start)
            }
        }

        viewModel.isLastPage.observe(viewLifecycleOwner){
            scrollListener.isLastPage = it
        }

        binding.photoGrid.addOnScrollListener(scrollListener)



        viewModel.photoLiveData.observe(viewLifecycleOwner) { photoListAdapter.submitList(it) }

        return binding.root
    }


}