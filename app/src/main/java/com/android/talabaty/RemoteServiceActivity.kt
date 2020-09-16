package com.android.talabaty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.android.talabaty.adapter.RemoteServiceAdapter
import com.android.talabaty.adapter.RemoteServicePagerAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Digital
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.viewModel.DigitalServiceViewModel
import kotlinx.android.synthetic.main.activity_remote_service.*
import kotlinx.android.synthetic.main.activity_remote_service.swipeRefresh
import kotlinx.android.synthetic.main.activity_remote_service.tvNotFound
import kotlinx.android.synthetic.main.title_toolbar.*
import java.util.*
import kotlin.collections.ArrayList

class RemoteServiceActivity : AppCompatActivity() {
    val TAG = "RemoteServiceActivity"
    private lateinit var viewModel: DigitalServiceViewModel
    var currentPage = 0
    var data: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_service)
        data = ArrayList<Int>()

        initViewModel()
        viewModel.digitals()
        handleToolbar()
        swipeToRefresh()
        setupObserver()
        initViewPager()

        cardArrowRight.setOnClickListener {
            if (currentPage == 2) {
                currentPage = -1
            }
            currentPage += 1
            viewPager.setCurrentItem(currentPage, true)
        }

        cardArrowLeft.setOnClickListener {
            if (currentPage == 0) {
                currentPage = data!!.size
            }

            currentPage -= 1
            viewPager.setCurrentItem(currentPage, true)
        }
    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.digital_services)

    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(DigitalServiceViewModel::class.java)
    }

    private fun setupObserver() {

        viewModel.getAllDigitals().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh.isRefreshing = false

                        it.data?.let { users ->
                            if (users.digitals.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else {
                                initRecycleView(users.digitals)
                            }

                        }
                    }
                    Status.LOADING -> {
                        tvNotFound.visibility = View.GONE

                        swipeRefresh.isRefreshing = true
                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE

                        swipeRefresh.isRefreshing = false

                    }
                }

            }
        )
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {
            viewModel.digitals()
        }

    }

    private fun initRecycleView(data: List<Digital>) {

        val adapter = RemoteServiceAdapter(this, data)
        val linearLayoutManager = GridLayoutManager(this, 3)
        rvRemote.layoutManager = linearLayoutManager
        rvRemote.adapter = adapter
        rvRemote.setHasFixedSize(true)

    }

    private fun initViewPager() {
        data?.add(R.drawable.img_wp)
        data?.add(R.drawable.img_fish)
        data?.add(R.drawable.img_newest)
        val adapter = RemoteServicePagerAdapter(this, data!!)
        with(viewPager) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            //to disable touch swiping
            isUserInputEnabled = false
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            this.adapter = adapter

        }

        val timer = Timer()
        val handler = Handler()
        val runnable = Runnable {
            if (currentPage == 2) {
                currentPage = -1
            }
            currentPage += 1
            viewPager.setCurrentItem(currentPage, true)
            viewPager.animation
        }

        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }
        timer.schedule(timerTask, 1, 4000)

    }

}