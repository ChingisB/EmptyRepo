package com.example.util

import android.text.Editable
import android.text.TextWatcher

class MaskWatcher(private val mask: String) : TextWatcher {

    private var isDeleting = false
    private var isRunning = false


    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) { isDeleting = count > after }

    override fun onTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun afterTextChanged(editable: Editable?) {
        if (isRunning || isDeleting || editable == null) return

        if (editable.length > mask.length) editable.replace(mask.length, editable.length, "")

        isRunning = true
        if (editable.length < mask.length && mask[editable.length] != '#') editable.append(mask[editable.length])
        else if (mask[editable.length - 1] != '#') {
            editable.insert(editable.length - 1, mask, editable.length - 1, editable.length)
        }

        isRunning = false

    }

    companion object {
        fun buildDateMask(): MaskWatcher {
            return MaskWatcher("####-##-##")
        }
    }
}