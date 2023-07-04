package com.example.basicapplication


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.basicapplication.databinding.ActivityMainBinding
import com.example.basicapplication.ui.bottom_navigation.BottomNavigationFragment
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

}