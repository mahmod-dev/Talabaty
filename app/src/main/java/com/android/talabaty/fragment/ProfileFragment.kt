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
import com.android.talabaty.auth.SignInActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val btnProfileLogout= root.findViewById<View>(R.id.btnProfileLogout)
        val rlLogout= root.findViewById<View>(R.id.rlLogout)

        btnProfileLogout.setOnClickListener {
            startActivity(Intent(activity,SignInActivity::class.java))
        }

        rlLogout.setOnClickListener {
            startActivity(Intent(activity,SignInActivity::class.java))
        }
        return root
    }
}