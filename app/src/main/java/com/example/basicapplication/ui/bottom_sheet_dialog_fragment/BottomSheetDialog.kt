package com.example.basicapplication.ui.bottom_sheet_dialog_fragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.databinding.BottomSheetDialogBinding
import com.example.basicapplication.ui.make.MakeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class BottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: MakeViewModel.Factory
    private val binding by lazy { BottomSheetDialogBinding.inflate(layoutInflater) }
    private val viewModel: MakeViewModel by activityViewModels { viewModelFactory }
    private val galleryResultLauncher = createGalleryResultLauncher()
    private val requestPermissionsLauncher = createRequestPermissionsLauncher()
    private val cameraResultLauncher = createCameraResultLauncher()

    private fun createCameraResultLauncher() = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            viewModel.setImageUri(cameraImageUri)
            dismiss()
        }
    }

    private fun createRequestPermissionsLauncher() = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        Log.e("some permissions", it.toString())
    }

    private fun createGalleryResultLauncher() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { viewModel.setImageUri(it) }
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.makeNewPhotoButton.setOnClickListener { getImageFromCamera() }
        binding.choosePictureFromGalleryButton.setOnClickListener { getImageFromGallery() }
    }


    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    private fun getImageFromCamera() {
        val context = requireContext()
        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissionsLauncher.launch(permission)
        } else {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.TITLE, "New picture from camera")
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Some new picture")
            val createdUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (createdUri != null) {
                cameraImageUri = createdUri
                cameraResultLauncher.launch(cameraImageUri)
            }
        }
    }

    companion object {
        lateinit var cameraImageUri: Uri
    }
}