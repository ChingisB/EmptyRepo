package com.example.basicapplication.ui.make

import android.content.Context
import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedImageViewModel
import com.example.basicapplication.dagger.DaggerViewModelFactory
import com.example.basicapplication.databinding.FragmentMakeBinding
import com.example.basicapplication.ui.bottom_sheet_dialog_fragment.ChoosePictureUploadModeBottomSheetDialog
import com.example.util.Resource
import java.io.File
import javax.inject.Inject

class MakeFragment : BaseFragment<FragmentMakeBinding, MakeViewModel>() {

    @Inject lateinit var viewModelFactory: DaggerViewModelFactory
    override val viewModel: MakeViewModel by activityViewModels { viewModelFactory }
    private val sharedImageViewModel: SharedImageViewModel by activityViewModels { viewModelFactory }
    private var imageFile: File? = null
//    TODO BottomSheetDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedImageViewModel.clearImageFile()
    }

    override fun getViewBinding() = FragmentMakeBinding.inflate(layoutInflater)

    override fun setupViews() {
        viewModel.new = false
        viewModel.popular = false
    }

    override fun setupListeners() {
        binding.saveButton.setOnClickListener {
            if (imageFile != null) viewModel.createPhoto(imageFile!!, binding.postName.text.toString(), binding.description.text.toString())
        }
        binding.uploadImageButton.setOnClickListener { ChoosePictureUploadModeBottomSheetDialog().show(childFragmentManager, "") }
        binding.cancel.setOnClickListener { childFragmentManager.popBackStack() }
        binding.checkBoxNewTag.setOnClickListener { viewModel.new = binding.checkBoxNewTag.isChecked }
        binding.checkBoxPopularTag.setOnClickListener { viewModel.popular = binding.checkBoxPopularTag.isChecked }
    }

    override fun observeData() {
        super.observeData()
        sharedImageViewModel.imageLiveData.observe(viewLifecycleOwner) {
            imageFile = it
            if (it != null) binding.image.setImageURI(it.toUri())
            binding.uploadImageButton.text = getText(R.string.change_photo)
        }
        viewModel.createPhotoResultLiveData.observe(viewLifecycleOwner) {
            showToastShort(
                when (it) {
                    is Resource.Success -> R.string.create_photo_success
                    is Resource.Error -> R.string.upload_photo_error
                    is Resource.Loading -> R.string.upload_photo_loading
                }
            )
        }
    }

}