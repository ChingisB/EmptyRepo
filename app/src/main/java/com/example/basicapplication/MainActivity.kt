package com.example.basicapplication


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.basicapplication.databinding.ActivityMainBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.example.basicapplication.ui.welcome.WelcomeFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    @Inject
    lateinit var tokenViewModelFactory: TokenViewModel.Factory


    private val tokenViewModel by viewModels<TokenViewModel> { tokenViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        appComponent.injectActivity(this)


        tokenViewModel.tokenLiveData.observe(this) {
            when (it) {
                null -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, WelcomeFragment())
                    .addToBackStack("welcome")
                    .commit()
                else -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, BottomNavigationFragment())
                    .commit()
            }
        }


        setContentView(binding.root)


    }

}