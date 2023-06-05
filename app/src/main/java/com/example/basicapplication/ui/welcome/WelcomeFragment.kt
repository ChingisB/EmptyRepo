package com.example.basicapplication.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.basicapplication.R
import com.example.basicapplication.databinding.FragmentWelcomeBinding
import com.example.basicapplication.ui.signIn.SignInFragment
import com.example.basicapplication.ui.signUp.SignUpFragment


class WelcomeFragment : Fragment() {


    lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWelcomeBinding.inflate(inflater)


        binding.navigateToSignin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment()).addToBackStack("signin").commit()
        }

        binding.navigateToSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment()).addToBackStack("signup").commit()
        }

        return binding.root
    }
}