package com.example.basicapplication.util

import android.content.Context
import com.example.domain.resource_provider.ResourceProvider
import javax.inject.Inject

class AndroidResourceProvider @Inject constructor(private val context: Context): ResourceProvider {
    override fun getMessage(messageKey: String): String {
        val resId = context.resources.getIdentifier(
            messageKey,
            "string",
            context.packageName
        )
        return context.getString(resId)
    }
}