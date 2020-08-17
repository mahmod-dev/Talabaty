package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.talabaty.R
import com.android.talabaty.SettingsActivity


class ProfileFragment : Fragment() {
    var settings: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        settings = root.findViewById(R.id.tv_settings)
        settings?.setOnClickListener { startActivity(Intent(activity, SettingsActivity::class.java)) }
        return root
    }
}