package com.android.talabaty.fragment.mainTab

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.talabaty.R
import com.android.talabaty.StoreDetailsActivity

class ResturentFragment : Fragment() {
    var views: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_resturent, container, false)
        views = root.findViewById(R.id.first_view)
        views?.setOnClickListener { startActivity(Intent(activity, StoreDetailsActivity::class.java)) }
        return root
    }
}