package com.example.basicapplication.ui.profile_settings

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedImageViewModel
import com.example.basicapplication.SharedUserViewModel
import com.example.basicapplication.databinding.FragmentProfileSettingsBinding
import com.example.basicapplication.ui.bottom_sheet_dialog_fragment.ChoosePictureUploadModeBottomSheetDialog
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.util.MaskWatcher
import com.example.util.Resource
import javax.inject.Inject

class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>() {

    @Inject lateinit var viewModelFactory: ProfileSettingsViewModel.Factory
    @Inject lateinit var sharedImageViewModelFactory: SharedImageViewModel.Factory
    override val viewModel: ProfileSettingsViewModel by viewModels { viewModelFactory }
    private val sharedImageViewModel: SharedImageViewModel by activityViewModels { sharedImageViewModelFactory }
    private val sharedUserViewModel: SharedUserViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedImageViewModel.clearImageFile()
    }

//  TODO unused attribute
    override fun getViewBinding() = FragmentProfileSettingsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        root.setOnClickListener { }
        signOutButton.setOnClickListener {
            viewModel.signOut()
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, SignInFragment()).commit()
        }
        cancelButton.setOnClickListener { parentFragmentManager.popBackStack() }
        saveButton.setOnClickListener {
            viewModel.submitEvent(ProfileSettingFormEvent.Submit)
            if (!viewModel.checkFormErrors()) parentFragmentManager.popBackStack()
        }
        avatarCard.setOnClickListener { ChoosePictureUploadModeBottomSheetDialog().show(childFragmentManager, "") }
        birthday.addTextChangedListener(MaskWatcher.buildDateMask())

        username.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.UsernameChanged(it.toString())) }
        birthday.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.BirthdayChanged(it.toString())) }
        email.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.EmailChanged(it.toString())) }
        oldPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.OldPasswordChanged(it.toString())) }
        newPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.NewPasswordChanged(it.toString())) }
        confirmPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.ConfirmPasswordChanged(it.toString())) }
    }.let { }

    override fun observeData() {
        sharedUserViewModel.userLiveData.observe(viewLifecycleOwner) {
            viewModel.userId = it.id
            binding.email.setText(it.email)
            binding.username.setText(it.username)
            binding.birthday.setText(it.birthday)
            viewModel.setInitialFormState(it.username, binding.birthday.text.toString(), it.email)
        }

        viewModel.profileSettingFormState.observe(viewLifecycleOwner) { formState ->
            formState.usernameError?.let { binding.usernameLayout.error = it}
            formState.birthdayError?.let { binding.birthdayLayout.error = it }
            formState.emailError?.let { binding.emailLayout.error = it }
            formState.oldPasswordError?.let { binding.oldPasswordLayout.error = it }
            formState.newPasswordError?.let { binding.newPasswordLayout.error = it }
            formState.confirmPasswordError?.let { binding.confirmPasswordLayout.error = it }
        }

        viewModel.updatePasswordResult.observe(viewLifecycleOwner) { if (it) showToastShort(R.string.password_changed) }

        viewModel.updateUserResult.observe(viewLifecycleOwner) {
            if (it is Resource.Success){
                sharedUserViewModel.setUser(it.data)
                showToastShort(R.string.user_info_changed)
            }
        }

        viewModel.updateAvatarResult.observe(viewLifecycleOwner){
            val newImageUri = sharedImageViewModel.imageLiveData.value
            if(it && newImageUri != null){
                sharedUserViewModel.setAvatar(newImageUri.toUri())
                viewModel.submitEvent(ProfileSettingFormEvent.AvatarChanged(null))
            }
        }

        sharedImageViewModel.imageLiveData.observe(viewLifecycleOwner){
            if(it != null){
                binding.avatarImage.setImageURI(it.toUri())
                binding.avatarImage.scaleType = ImageView.ScaleType.FIT_CENTER
                viewModel.submitEvent(ProfileSettingFormEvent.AvatarChanged(it.toUri()))
            }
        }

        sharedUserViewModel.avatarLiveData.observe(viewLifecycleOwner){
            Glide.with(this).load(it).into(binding.avatarImage)
            binding.avatarImage.scaleType = ImageView.ScaleType.FIT_CENTER
        }

    }
}