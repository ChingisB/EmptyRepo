package com.example.basicapplication


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.basicapplication.databinding.ActivityMainBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject lateinit var tokenViewModelFactory: TokenViewModel.Factory
    private val tokenViewModel: TokenViewModel by viewModels { tokenViewModelFactory }
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
        setContentView(binding.root)

        val observer = object : Observer<String?> {
            override fun onChanged(value: String?) {
                if (value == null) {
                    tokenViewModel.tokenLiveData.removeObserver(this)
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.activityFragmentContainer, SignInFragment()).commit()
                }
            }

        }

        tokenViewModel.tokenLiveData.observe(this, observer)
    }

}