package com.android.talabaty.model

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.talabaty.R

class BottomNavigationHandler(
    var activity: Activity,
    var img: ImageView,
    var text: TextView,
    var imageUnselected: Int,
    var imageSelected: Int,
    var index: Int
) {

    fun setIsSelected(isSelected: Boolean) {
        if (isSelected) {
            img.setImageResource(imageSelected)
            text.visibility = View.VISIBLE
            text.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            img.isSelected = true
        } else {
            img.setImageResource(imageUnselected)
          //  img.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
            text.visibility = View.GONE
            img.isSelected = false
        }
    }

}
