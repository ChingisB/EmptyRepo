package com.example.basicapplication.ui.newPhotos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.basicapplication.MainActivity
import com.example.basicapplication.adapter.PhotoListAdapter
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentNewPhotosBinding
import javax.inject.Inject


class NewPhotosFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NewPhotosViewModel.Factory


    val viewModel by viewModels<NewPhotosViewModel> { viewModelFactory }


    lateinit var photoListAdapter: PhotoListAdapter


    lateinit var binding: FragmentNewPhotosBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Context).appComponent.injectNewPhotosFragment(this)
        viewModel.fetch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentNewPhotosBinding.inflate(inflater)

        photoListAdapter = PhotoListAdapter(emptyList(), {})

        binding.photoGrid.apply {
            adapter = photoListAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }



        viewModel.photoLiveData.observe(viewLifecycleOwner) { photoListAdapter.submitList(it) }

        return binding.root
    }


}