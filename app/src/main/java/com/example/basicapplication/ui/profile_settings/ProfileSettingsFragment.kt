package com.example.basicapplication.ui.profile_settings

import android.content.Context
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.base.BaseFragment
import com.example.basicapplication.MainApplication
import com.example.basicapplication.R
import com.example.basicapplication.SharedUserViewModel
import com.example.basicapplication.databinding.FragmentProfileSettingsBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import com.example.util.MaskWatcher
import com.example.util.Resource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>() {

    @Inject
    lateinit var viewModelFactory: ProfileSettingsViewModel.Factory
    override val viewModel: ProfileSettingsViewModel by viewModels { viewModelFactory }
    private val sharedUserViewModel: SharedUserViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.appComponent.inject(this)
    }
// TODO unused attribute
    override fun getViewBinding() = FragmentProfileSettingsBinding.inflate(layoutInflater)

    override fun setupListeners() = with(binding) {
        super.setupListeners()
        signOutButton.setOnClickListener {
            viewModel.signOut()
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, SignInFragment()).commit()
        }
        cancelButton.setOnClickListener { parentFragmentManager.popBackStack() }
        saveButton.setOnClickListener { viewModel.submitEvent(ProfileSettingFormEvent.Submit) }
        birthday.addTextChangedListener(MaskWatcher.buildDateMask())

        username.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.UsernameChanged(it.toString())) }
        birthday.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.BirthdayChanged(it.toString())) }
        email.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.EmailChanged(it.toString())) }
        oldPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.OldPasswordChanged(it.toString())) }
        newPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.NewPasswordChanged(it.toString())) }
        confirmPassword.addTextChangedListener { viewModel.submitEvent(ProfileSettingFormEvent.ConfirmPasswordChanged(it.toString())) }
    }.let { Unit }

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
        viewModel.updatePasswordResult.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), R.string.password_changed, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.updateUserResult.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                Toast.makeText(requireContext(), R.string.user_info_changed, Toast.LENGTH_SHORT).show()
            }
        }

    }
}