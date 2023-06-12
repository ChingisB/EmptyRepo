package com.example.basicapplication.data.data_source.api

import com.example.basicapplication.data.data_source.api.service.AuthenticationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class AuthenticatorImpl @Inject constructor(
    private val authenticationServiceProvider: Provider<AuthenticationService>,
    private val tokenManager: TokenManager
) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (refreshToken())
            return response.request().newBuilder().removeHeader("Authorization")
                .addHeader("Authorization", tokenManager.getAccessToken()!!).build()
        return null
    }

    private fun refreshToken(): Boolean {
        val token = tokenManager.getRefreshToken()


        val service = authenticationServiceProvider.get()

        token?.let {
            try {
                val response = service.refreshToken(refreshToken = it)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).toObservable().blockingFirst()
                tokenManager.saveAccessToken(response.accessToken)
                tokenManager.saveRefreshToken(response.refreshToken)
                return true
            } catch (e: NoSuchElementException) {
                tokenManager.deleteAccessToken()
                tokenManager.deleteRefreshToken()
                return false
            }
        }

        return false
    }
}