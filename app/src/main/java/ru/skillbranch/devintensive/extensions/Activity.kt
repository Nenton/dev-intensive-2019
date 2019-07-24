package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager

/**
 * @author Sergey Susev
 */

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val softKeyboardHeight = 128

    val rect = Rect()
    window.decorView.getWindowVisibleDisplayFrame(rect)
    val dm = window.decorView.resources.displayMetrics
    val heightDiff = window.decorView.bottom - rect.bottom

    return heightDiff > softKeyboardHeight * dm.density
}

fun Activity.isKeyboardClosed(): Boolean = !isKeyboardOpen()