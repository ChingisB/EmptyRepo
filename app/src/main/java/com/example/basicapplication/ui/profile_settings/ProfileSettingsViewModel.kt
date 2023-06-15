package com.example.basicapplication.ui.profile_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.util.BaseViewModel
import com.example.basicapplication.util.Constants
import javax.inject.Inject

class ProfileSettingsViewModel(private val tokenRepository: TokenRepository): BaseViewModel() {

    fun signOut(){
        tokenRepository.deleteRefreshToken()
        tokenRepository.deleteAccessToken()
    }


    class Factory @Inject constructor(private val tokenRepository: TokenRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = kotlin.runCatching{
            @Suppress("UNCHECKED_CAST")
            return ProfileSettingsViewModel(tokenRepository) as T
        }.getOrElse { error(Constants.unknownViewModelClassError) }
    }
}