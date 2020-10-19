package com.android.talabaty.util

import android.widget.TextView
import androidx.core.content.ContextCompat

object TextViewHelper {

    fun TextView.drawableStart(drawable: Int) {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    fun TextView.drawableEnd(drawable: Int) {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }

    fun TextView.textColor(textColor: Int) {
        this.setTextColor(ContextCompat.getColor(this.context, textColor))
    }

    fun TextView.backgroundTint(color: Int) {
        this.backgroundTintList = ContextCompat.getColorStateList(this.context, color)
    }

    fun TextView.backgroundDrawable(drawable: Int) {
        this.background = ContextCompat.getDrawable(this.context, drawable)
    }

    fun TextView.backgroundColor(color: Int) {
        this.setBackgroundColor(ContextCompat.getColor(this.context,color))
    }

}