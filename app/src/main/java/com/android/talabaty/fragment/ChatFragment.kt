package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.talabaty.ChatActivity
import com.android.talabaty.R

class ChatFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_chat, container, false)
      val  item1 = view.findViewById<View>(R.id.item1)
      val  item2 = view.findViewById<View>(R.id.item2)
      val  item3 = view.findViewById<View>(R.id.item3)
      val  item4 = view.findViewById<View>(R.id.item4)

        item1.setOnClickListener {
            startActivity(Intent(activity,ChatActivity::class.java))
        }

        item2.setOnClickListener {
            startActivity(Intent(activity,ChatActivity::class.java))
        }

        item3.setOnClickListener {
            startActivity(Intent(activity,ChatActivity::class.java))
        }
        item4.setOnClickListener {
            startActivity(Intent(activity,ChatActivity::class.java))
        }



        return view
    }
}