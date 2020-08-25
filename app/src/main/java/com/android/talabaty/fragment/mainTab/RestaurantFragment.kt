package com.android.talabaty.fragment.mainTab

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.talabaty.R
import com.android.talabaty.StoreDetailsActivity

class RestaurantFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_resturent, container, false)
       val res1 = root.findViewById<View>(R.id.res1)
       val res2 = root.findViewById<View>(R.id.res2)
       val res3 = root.findViewById<View>(R.id.res3)
       val res4 = root.findViewById<View>(R.id.res4)

        res1.setOnClickListener { startActivity(Intent(activity, StoreDetailsActivity::class.java)) }
        res2.setOnClickListener { startActivity(Intent(activity, StoreDetailsActivity::class.java)) }
        res3.setOnClickListener { startActivity(Intent(activity, StoreDetailsActivity::class.java)) }
        res4.setOnClickListener { startActivity(Intent(activity, StoreDetailsActivity::class.java)) }
        return root
    }


}