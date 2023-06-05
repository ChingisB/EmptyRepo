package com.example.basicapplication.ui.signIn

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.basicapplication.R
import com.example.basicapplication.appComponent
import com.example.basicapplication.databinding.FragmentSignInBinding
import com.example.basicapplication.ui.bottomNavFragment.BottomNavFragment
import javax.inject.Inject


class SignInFragment : Fragment() {


    private lateinit var binding: FragmentSignInBinding

    @Inject
    lateinit var viewModelFactory: SignInViewModel.Factory

    private val viewModel by viewModels<SignInViewModel> { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as Context).appComponent.injectSignInFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInBinding.inflate(inflater)

        binding.cancelButton.setOnClickListener { parentFragmentManager.clearBackStack("welcome") }

        binding.signInButton.setOnClickListener {
            viewModel.login(binding.email.text.toString(), binding.signInPassword.text.toString())
        }


        viewModel.loggedIn.observe(viewLifecycleOwner) { resource ->
            if (resource.data == true) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BottomNavFragment()).commit()
            }
        }

        return binding.root
    }


}