package com.android.talabaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.talabaty.adapter.BookAddressAdapter
import com.android.talabaty.dbUtil.Status
import com.android.talabaty.dbUtil.ViewModelFactory
import com.android.talabaty.model.Address
import com.android.talabaty.retrofit.ApiHelperImpl
import com.android.talabaty.retrofit.RetrofitBuilder
import com.android.talabaty.util.CustomMaterialDialog.getMaterialDialogInstance
import com.android.talabaty.viewModel.AddressBookViewModel
import kotlinx.android.synthetic.main.activity_all_address_book.*
import kotlinx.android.synthetic.main.activity_all_address_book.swipeRefresh
import kotlinx.android.synthetic.main.activity_all_address_book.tvNotFound
import kotlinx.android.synthetic.main.title_toolbar.*

class AllAddressBookActivity : AppCompatActivity() {
    private lateinit var viewModel: AddressBookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_address_book)
        initViewModel()
        handleToolbar()
        viewModel.allAddress()
        setupObserverAll()
        swipeToRefresh()
        setupObserverDelete()
        fabAdd.setOnClickListener {
            val intent = Intent(applicationContext, AddAddressBookActivity::class.java)
            intent.putExtra("type",0)
            startActivity(intent)
        }
    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService), application)
        ).get(AddressBookViewModel::class.java)


    }

    private fun setupObserverAll() {

        viewModel.getAllAddress().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            if (users.addresses.isEmpty()) {
                                tvNotFound.visibility = View.VISIBLE
                            } else
                                initRecycleView(users.addresses)
                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                        tvNotFound.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        tvNotFound.visibility = View.GONE
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)


                    }
                }

            }
        )
    }

    private fun setupObserverDelete() {

        viewModel.getDeleteAddress().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        swipeRefresh?.isRefreshing = false
                        it.data?.let { users ->
                            Toast.makeText(this, users.message, Toast.LENGTH_SHORT).show()

                        }
                    }
                    Status.LOADING -> {
                        swipeRefresh?.isRefreshing = true
                    }
                    Status.ERROR -> {
                        swipeRefresh?.isRefreshing = false
                        getMaterialDialogInstance(it.message!!)


                    }
                }

            }
        )
    }


    private fun initRecycleView(data: ArrayList<Address>) {
        val adapter = BookAddressAdapter(this, data)
        val linearLayoutManager = LinearLayoutManager(this)
        rcAddress.layoutManager = linearLayoutManager
        rcAddress.adapter = adapter
        rcAddress.setHasFixedSize(true)

        adapter!!.onItemEditClick = { id, obj ->
            val intent = (Intent(applicationContext, AddAddressBookActivity::class.java))
            intent.putExtra("id", obj.id)
            intent.putExtra("address", obj.address)
            intent.putExtra("type",1)

            startActivity(intent)

        }

        adapter!!.onItemDeleteClick = { id, position ->
            viewModel.deleteAddress(id)
            data.removeAt(position)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun swipeToRefresh() {
        swipeRefresh?.setOnRefreshListener {

            viewModel.allAddress()
        }

    }

    private fun handleToolbar() {
        imgArrowBack.setOnClickListener {
            finish()

        }

        tvTitleToolbar.text = getString(R.string.address_book)

    }

    override fun onStart() {
        super.onStart()
        viewModel.allAddress()

    }


}