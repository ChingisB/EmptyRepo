package com.example.basicapplication


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basicapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        appComponent.injectActivity(this)

        setContentView(binding.root)



    }

}