package com.android.talabaty

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.talabaty.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleBottomNav()


    }

    private fun handleBottomNav() {

        val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.itemId) {

                        R.id.navMain -> {
                            return true
                        }
                        R.id.navRequests -> {
                            return true
                        }
                        R.id.navChat -> {
                            return true
                        }

                        R.id.navCategory -> {
                            return true
                        }

                        R.id.navProfile -> {
                            return true
                        }
                    }
                    return false
                }
            }

        bottomNavMain.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }
}