package com.example.basicapplication.data.data_source.api

import com.example.basicapplication.data.data_source.api.service.AuthenticationService
import com.example.basicapplication.data.repository.token_repository.TokenRepository
import com.example.basicapplication.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthenticatorImpl @Inject constructor(
    private val authenticationServiceProvider: dagger.Lazy<AuthenticationService>,
    private val tokenRepository: TokenRepository
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (refreshToken())
            return response
                .request()
                .newBuilder()
                .removeHeader(Constants.AUTHORIZATION_HEADER)
                .addHeader(Constants.AUTHORIZATION_HEADER, "${Constants.AUTHORIZATION_TYPE} ${tokenRepository.getAccessToken()!!}")
                .build()
        return null
    }

    private fun refreshToken(): Boolean {
        val token = tokenRepository.getRefreshToken()

        val service = authenticationServiceProvider.get()

        token?.let {
            try {
                val response = service.refreshToken(refreshToken = it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).toObservable().blockingFirst()
                tokenRepository.saveTokens(accessToken = response.accessToken, refreshToken = response.refreshToken)
                return true
            } catch (e: NoSuchElementException) {
                tokenRepository.deleteTokens()
                return false
            }
        }

        return false
    }
}