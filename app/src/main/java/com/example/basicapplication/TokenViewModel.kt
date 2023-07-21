package com.example.basicapplication

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.BaseViewModel
import com.example.basicapplication.util.Constants
import javax.inject.Inject

class TokenViewModel @Inject constructor(sharedPreferences: SharedPreferences) : BaseViewModel() {


    private val _tokenLiveData = MutableLiveData<String?>()
    val tokenLiveData: LiveData<String?> = _tokenLiveData

    init {
        _tokenLiveData.postValue(sharedPreferences.getString(Constants.REFRESH_TOKEN_KEY, null))
        sharedPreferences.registerOnSharedPreferenceChangeListener { preferences, key ->
            if(key == Constants.REFRESH_TOKEN_KEY){ _tokenLiveData.postValue(preferences.getString(key, null)) }
        }
    }

}