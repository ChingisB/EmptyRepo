package com.example.basicapplication


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.basicapplication.databinding.ActivityMainBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenViewModelFactory: TokenViewModel.Factory


    lateinit var binding: ActivityMainBinding


    private val tokenViewModel by viewModels<TokenViewModel> { tokenViewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TODO make binding lazy
        binding = ActivityMainBinding.inflate(layoutInflater)

        MainApplication.appComponent.inject(this)

        tokenViewModel.tokenLiveData.observe(this) { token ->
            when (token) {
                null -> {}
                else ->{
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, BottomNavigationFragment()).commit()
                }
            }
        }


        setContentView(binding.root)


    }

}