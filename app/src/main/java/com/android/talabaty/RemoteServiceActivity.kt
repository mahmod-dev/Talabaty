package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_remote_service.*
import kotlinx.android.synthetic.main.title_toolbar.*

class RemoteServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_service)
        handleToolbar()
        onClickEvent()
    }

   private fun handleToolbar(){
       imgArrowBack.setOnClickListener {
           finish()

       }

       tvTitleToolbar.text = getString(R.string.digital_services)

    }

    private fun onClickEvent(){
        cardPromotion.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardMarketing.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardVideo.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardPhoto.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardCoding.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardCustomer.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardDesign.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardTranslate.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }

        cardOther.setOnClickListener {
            val intent = Intent(this,ElectronicServiceActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }
    }
}