package com.android.talabaty.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.android.talabaty.R
import com.android.talabaty.SettingsActivity
import com.android.talabaty.auth.SignInActivity
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.Helper
import com.android.talabaty.util.MyPreferences
import com.android.talabaty.viewModel.CartViewModel
import com.android.talabaty.viewModel.ProfileViewModel
import com.android.talabaty.viewModel.StoresViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    val TAG = "ProfileFragment"
    private lateinit var viewModel: ProfileViewModel
    var tvProfileMobile : TextView? = null
    var tvProfileName : TextView? = null
    var imgProfile : ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val btnProfileLogout = root.findViewById<View>(R.id.btnProfileLogout)
        val rlLogout = root.findViewById<View>(R.id.rlLogout)
        tvProfileMobile = root.findViewById(R.id.tvProfileMobile)
        tvProfileName = root.findViewById(R.id.tvProfileName)
        imgProfile = root.findViewById(R.id.imgProfile)
        MyPreferences.context = context
        initViewModel()
        viewModel.profile()
        btnProfileLogout.setOnClickListener {
            MyPreferences.setInt("isLogin", 0)

            startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finish()
        }

        rlLogout.setOnClickListener {
            MyPreferences.setInt("isLogin", 0)
            startActivity(Intent(activity, SignInActivity::class.java))
            activity?.finish()

        }

        setupObserver()
        return root
    }

    private fun setupObserver() {

        viewModel.getProfile().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { users ->
                            tvProfileName?.text = users.user.name
                            tvProfileMobile?.text = users.user.mobile
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

                    }
                    Status.ERROR -> {
                        Helper.showFilterDialog(activity!!, it.message!!).show()
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

}