package com.example.basicapplication

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class TokenViewModel(
    sharedPreferences: SharedPreferences
) : ViewModel() {


    private val _tokenLiveData = MutableLiveData<String?>()
    val tokenLiveData: LiveData<String?> = _tokenLiveData

    init {
//        TODO move to cons
        _tokenLiveData.postValue(sharedPreferences.getString("RefreshToken", null))
        sharedPreferences.registerOnSharedPreferenceChangeListener { preferences, key ->
            if(key == "RefreshToken"){
                _tokenLiveData.postValue(preferences.getString(key, null))
            }
        }
    }


    class Factory @Inject constructor(
        private val preferences: SharedPreferences
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TokenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TokenViewModel(preferences) as T
            }
            //        TODO move to cons
            throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
        }
    }
}