package com.example.basicapplication.ui.bottom_sheet_dialog_fragment

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.basicapplication.MainApplication
import com.example.basicapplication.SharedImageViewModel
import com.example.basicapplication.databinding.BottomSheetDialogBinding
import com.example.basicapplication.util.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChoosePictureUploadModeBottomSheetDialog : BottomSheetDialogFragment() {

    private val binding by lazy { BottomSheetDialogBinding.inflate(layoutInflater) }
    private val viewModel: SharedImageViewModel by activityViewModels()
    private val galleryResultLauncher = createGalleryResultLauncher()
    private val requestPermissionsLauncher = createRequestPermissionsLauncher()
    private val cameraResultLauncher = createCameraResultLauncher()


    private fun createGalleryResultLauncher() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { viewModel.setImageFile(it) }
        dismiss()
    }

    private fun createRequestPermissionsLauncher() = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if(it.getOrDefault(Manifest.permission.CAMERA, false)){
            getImageFromCamera()
        } else { dismiss() }
    }

    private fun createCameraResultLauncher() = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            viewModel.setImageFile(imageUri)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        setupListeners()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun setupListeners() {
        binding.makeNewPhotoButton.setOnClickListener { getImageFromCamera() }
        binding.choosePictureFromGalleryButton.setOnClickListener { getImageFromGallery() }
    }

    private fun getImageFromGallery() =
        galleryResultLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))


    private fun getImageFromCamera() {
        if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissionsLauncher.launch(permission)
        } else {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.TITLE, Constants.NEW_MEDIA_FILE_KEY)
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, Constants.NEW_MEDIA_FILE_DESCRIPTION)
            val createdUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if(createdUri != null){
                imageUri = createdUri
                cameraResultLauncher.launch(imageUri)
            }
        }
    }

    companion object {
        lateinit var imageUri: Uri
    }
}