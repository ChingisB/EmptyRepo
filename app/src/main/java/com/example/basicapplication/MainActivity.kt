package com.example.basicapplication


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.example.basicapplication.dagger.DaggerViewModelFactory
import com.example.basicapplication.databinding.ActivityMainBinding
import com.example.basicapplication.ui.sign_in.SignInFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: DaggerViewModelFactory
    private val tokenViewModel: TokenViewModel by viewModels { viewModelFactory }
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
                    supportFragmentManager.commit { replace(R.id.activityFragmentContainer, SignInFragment()) }
                }
            }

        }

        tokenViewModel.tokenLiveData.observe(this, observer)
    }

}