package com.example.basicapplication.ui.make

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentMakeBinding
import com.example.basicapplication.base.BaseFragment
import com.example.basicapplication.ui.bottom_sheet_dialog_fragment.BottomSheetDialog
import com.example.basicapplication.util.Resource
import javax.inject.Inject

class MakeFragment : BaseFragment<FragmentMakeBinding, MakeViewModel>() {

    @Inject
    lateinit var viewModelFactory: MakeViewModel.Factory
    override val viewModel: MakeViewModel by activityViewModels { viewModelFactory }
//    TODO BottomSheetDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun setupViews() {
        super.setupViews()
        BottomSheetDialog().show(parentFragmentManager, "")
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.saveButton.setOnClickListener {
            viewModel.createPhoto(binding.postName.text.toString(), binding.description.text.toString())
        }

        binding.cancel.setOnClickListener { childFragmentManager.popBackStack() }
        binding.checkBoxNewTag.setOnClickListener{ viewModel.new = binding.checkBoxNewTag.isChecked }
        binding.checkBoxPopularTag.setOnClickListener { viewModel.popular = binding.checkBoxPopularTag.isChecked }
    }

    override fun observeData() {
        super.observeData()
        viewModel.imageLiveData.observe(viewLifecycleOwner) {
            binding.image.setImageURI(it)
        }
        viewModel.createPhotoResultLiveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), R.string.create_photo_success, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getViewBinding() = FragmentMakeBinding.inflate(layoutInflater)

}