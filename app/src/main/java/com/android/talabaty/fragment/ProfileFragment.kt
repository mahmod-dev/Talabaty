package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.talabaty.MyCardActivity
import com.android.talabaty.R
import com.android.talabaty.auth.SignInActivity
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.util.Helper
import com.android.talabaty.util.Helper.showLogoutDialog
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.switchmaterial.SwitchMaterial


class ProfileFragment : Fragment() {
    val TAG = "ProfileFragment"
    private lateinit var viewModel: ProfileViewModel
    var tvProfileMobile: TextView? = null
    var tvProfileName: TextView? = null
    var tvWalletAmount: TextView? = null
    var tvCardAmount: TextView? = null
    var tvShowCard: TextView? = null
    var imgProfile: ImageView? = null
    var swStores: SwitchMaterial? = null
    var swOrders: SwitchMaterial? = null
    var swOffers: SwitchMaterial? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val rlLogout = root.findViewById<View>(R.id.rlLogout)
        tvProfileMobile = root.findViewById(R.id.tvProfileMobile)
        tvProfileName = root.findViewById(R.id.tvProfileName)
        imgProfile = root.findViewById(R.id.imgProfile)
        swOrders = root.findViewById(R.id.swOrders)
        swStores = root.findViewById(R.id.swStores)
        swOffers = root.findViewById(R.id.swOffers)
        tvShowCard = root.findViewById(R.id.tvShowCard)
        tvWalletAmount = root.findViewById(R.id.tvWalletAmount)
        tvCardAmount = root.findViewById(R.id.tvCardAmount)
        swipeRefresh = root.findViewById(R.id.swipeRefresh)

        MyPreferences.context = context
        initViewModel()
        viewModel.profile()

        rlLogout.setOnClickListener {
            activity?.showLogoutDialog()?.show()
                    Handler().postDelayed({

                        MyPreferences.setInt("isLogin", 0)
                        startActivity(Intent(activity, SignInActivity::class.java))
                        activity?.finish()

        }, 3000)


        }

        tvShowCard?.setOnClickListener {
            startActivity(Intent(activity, MyCardActivity::class.java))

        }

        swipeToRefresh()
        setupObserver()
        handelSettings()
        setupObserverChangeNotification()
        return root
    }

    private fun setupObserver() {

        viewModel.getProfile().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false

                        it.data?.let { users ->
                            tvProfileName?.text = users.user.name
                            tvProfileMobile?.text = users.user.mobile
                            tvWalletAmount?.text = users.user.wallet_amount.toString()
                            tvCardAmount?.text = users.user.payment_cards_count.toString()
                            if (users.user.image_profile.isNotEmpty()) {
                                Glide.with(context!!).load(users.user.image_profile)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ic_icon_loading)
                                    .error(R.drawable.white)
                                    .into(imgProfile!!)
                            }

                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false

                        activity?.getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }

    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            activity!!,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), activity!!.application)
        ).get(ProfileViewModel::class.java)
    }


    private fun handelSettings() {

        swOrders?.isChecked = MyPreferences.getBool("swOrders")
        swOffers?.isChecked = MyPreferences.getBool("swOffers")
        swStores?.isChecked = MyPreferences.getBool("swStores")

        swOrders?.setOnCheckedChangeListener { btn, isChecked ->
            MyPreferences.setBool("swOrders", isChecked)
            if (isChecked)
                viewModel.changeNotificationStatus(1, "orders")
            else
                viewModel.changeNotificationStatus(0, "orders")
        }
        swOffers?.setOnCheckedChangeListener { btn, isChecked ->
            MyPreferences.setBool("swOffers", isChecked)
            if (isChecked)
                viewModel.changeNotificationStatus(1, "offers")
            else
                viewModel.changeNotificationStatus(0, "offers")
        }

        swStores?.setOnCheckedChangeListener { btn, isChecked ->
            MyPreferences.setBool("swStores", isChecked)
            if (isChecked)
                viewModel.changeNotificationStatus(1, "stores")
            else
                viewModel.changeNotificationStatus(0, "stores")

        }
    }

    private fun setupObserverChangeNotification() {

        viewModel.getChangeNotificationStatus().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->

                           Toast.makeText(context,users.message,Toast.LENGTH_SHORT).show()

                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        activity?.getMaterialDialogInstance(it.message!!)

                        Log.e(TAG, "setupObserver: " + it.message)
                    }
                }
            }
        )
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {

            viewModel.profile()
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.profile()
        setupObserver()
        handelSettings()
    }

}