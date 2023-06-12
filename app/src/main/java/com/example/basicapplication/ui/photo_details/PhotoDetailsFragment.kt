package com.example.basicapplication.ui.photo_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.basicapplication.SharedPhotoViewModel
import com.example.basicapplication.data.data_source.api.Config
import com.example.basicapplication.databinding.FragmentPhotoDetailsBinding


class PhotoDetailsFragment : Fragment() {


    private lateinit var binding: FragmentPhotoDetailsBinding


    val viewModel: SharedPhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoDetailsBinding.inflate(inflater)

        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                photoName.text = it.name
                description.text = it.description
                postDate.text = it.dateCreate
                Glide.with(image).load(Config.mediaUrl + it.image?.name).into(image)
            }
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        return binding.root
    }

}