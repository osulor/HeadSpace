package com.example.headspacechallenge.ui

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.headspacechallenge.R
import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.data.remote.Webservices
import com.example.headspacechallenge.data.repository.FeatureRepositoryImpl
import com.example.headspacechallenge.database.HeadspaceDB
import com.example.headspacechallenge.ui.adapter.ItemAdapter
import com.example.headspacechallenge.viewmodel.FeatureViewModel
import com.example.headspacechallenge.viewmodel.FeatureViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FeatureViewModel
    lateinit var itemAdapter: ItemAdapter

    lateinit var database : HeadspaceDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(this,HeadspaceDB::class.java,"headspace-database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        setUpRecyclerView()

        viewModel = ViewModelProviders.of(
            this,
            FeatureViewModelFactory(FeatureRepositoryImpl(Webservices.instance))
        )
            .get(FeatureViewModel::class.java)
        viewModel.items.observe(this, Observer {
            itemAdapter.featureList.clear()
            itemAdapter.featureList.addAll(it)
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    database.featureDAO().insertAll(it)
                }
            }

            itemAdapter.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(this, Observer {
            tvMessage.text = it
        })
        viewModel.loadingState.observe(this, Observer {
            when (it) {
                FeatureViewModel.LoadingState.LOADING -> displayProgressbar()
                FeatureViewModel.LoadingState.SUCCESS -> displayList()
                FeatureViewModel.LoadingState.ERROR -> displayMessageContainer()
                else -> displayMessageContainer()
            }
        })

        retrieveData()

        btnRetry.setOnClickListener {
            viewModel.getPicsFromAPI()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.getPicsFromAPI()
        }


    }

    fun retrieveData(){
        if(isNetworkAvailable(this)){
            viewModel.getPicsFromAPI()
        } else {
            var items = listOf<FeatureModel>()
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    items = database.featureDAO().getAll()
                }
            }
            viewModel.items.value = items
            Toast.makeText(this,"NO INTERNET",Toast.LENGTH_LONG).show()
        }
    }

    private fun displayProgressbar() {
        if (!swipeRefresh.isRefreshing) {
            progressbar.visibility = View.VISIBLE
            rvAlbum.visibility = View.GONE
            llMessageContainer.visibility = View.GONE
        }
    }

    private fun displayMessageContainer() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
        llMessageContainer.visibility = View.VISIBLE
        rvAlbum.visibility = View.GONE
        progressbar.visibility = View.GONE
    }

    private fun displayList() {
        if (swipeRefresh.isRefreshing) {
            Toast.makeText(this, "Data is Refreshed", Toast.LENGTH_LONG).show()
            swipeRefresh.isRefreshing = false
        }
        llMessageContainer.visibility = View.GONE
        rvAlbum.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
    }

    private fun setUpRecyclerView() {
        rvAlbum.layoutManager = GridLayoutManager(this, 2)
        itemAdapter = ItemAdapter(mutableListOf())
        rvAlbum.adapter = itemAdapter
    }


    fun isNetworkAvailable(activity: Activity): Boolean {
        val connectivityManager=activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}
