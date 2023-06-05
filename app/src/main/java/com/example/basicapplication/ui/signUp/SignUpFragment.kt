package com.example.basicapplication.ui.signUp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentSignUpBinding
import javax.inject.Inject

class SignUpFragment : Fragment() {


    lateinit var binding: FragmentSignUpBinding


    @Inject
    lateinit var viewModelFactory: SignUpViewModel.Factory


    private val viewModel by viewModels<SignUpViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Context).appComponent.injectSignUpFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener {
            parentFragmentManager.clearBackStack("welcome")
        }

        binding.signUp.setOnClickListener {
            viewModel.signUp(
                username = binding.username.text.toString(),
                birthday = binding.birthday.text.toString(),
                email = binding.email.text.toString(),
                password = binding.password.text.toString(),
                confirmPassword = binding.confirmPassword.text.toString()
            )
        }

        return binding.root
    }

}