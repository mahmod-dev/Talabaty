package com.android.talabaty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.talabaty.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*


open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
       // window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        handleBottomNav()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            MainFragment()
        ).commit()
    }

    private fun handleBottomNav() {

        val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                var selectedFragment: Fragment? = null

                when (item.itemId) {

                    R.id.navMain -> {
                        selectedFragment = MainFragment()

                    }
                    R.id.navRequests -> {
                        selectedFragment = OrdersFragment()
                    }
                    R.id.navChat -> {
                        selectedFragment = ChatFragment()
                    }

                    R.id.navCategory -> {
                        selectedFragment = CategoriesFragment()
                    }

                    R.id.navProfile -> {
                        selectedFragment = ProfileFragment()
                    }
                }

                supportFragmentManager.beginTransaction().replace(
                    R.id.fragmentContainer,
                    selectedFragment!!
                ).commit()

                 true
            }

        bottomNavMain.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

}