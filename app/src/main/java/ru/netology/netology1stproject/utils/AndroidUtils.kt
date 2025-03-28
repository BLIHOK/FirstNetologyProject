package ru.netology.netology1stproject.utils

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    fun changeCountersImagin(counter: Int): String {

        return when (counter) {
            in 0..999 -> counter.toString()
            in 1000..9999 ->
                if ((counter % 1000) == 0) {
                    (counter / 1000).toString() + "K"
                } else if (counter % 1000 < 100) {
                    (counter / 1000).toString() + "K"
                } else (counter / 1000).toString() + "." + ((counter / 100) % 10).toString() + "K"

            in 10000..999999 -> (counter / 1000).toString() + "K"

            in 1000000..10000000 ->
                if ((counter % 1000000) == 0) {
                    (counter / 1000000).toString() + "M"
                } else if (counter % 1000000 < 100000) {
                    (counter / 1000000).toString() + "M"
                } else (counter / 1000000).toString() + "." + ((counter / 100000) % 10).toString() + "M"

            else -> "0"
        }
    }

    fun HideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun View.focusAndShowKeyboard() {
        /**
         * This is to be called when the window already has focus.
         */
        fun View.showTheKeyboardNow() {
            if (isFocused) {
                post {
                    // We still post the call, just in case we are being notified of the windows focus
                    // but InputMethodManager didn't get properly setup yet.
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        requestFocus()
        if (hasWindowFocus()) {
            // No need to wait for the window to get focus.
            showTheKeyboardNow()
        } else {
            // We need to wait until the window gets focus.
            viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        // This notification will arrive just before the InputMethodManager gets set up.
                        if (hasFocus) {
                            this@focusAndShowKeyboard.showTheKeyboardNow()
                            // It’s very important to remove this listener once we are done.
                            viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }
}

