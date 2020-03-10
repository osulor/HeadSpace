package com.example.headspacechallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.headspacechallenge.R
import com.example.headspacechallenge.data.remote.Webservices
import com.example.headspacechallenge.data.repository.FeatureRepositoryImpl
import com.example.headspacechallenge.database.DatabaseProvider
import com.example.headspacechallenge.ui.adapter.ItemAdapter
import com.example.headspacechallenge.viewmodel.FeatureViewModel
import com.example.headspacechallenge.viewmodel.FeatureViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FeatureViewModel
    lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val database = DatabaseProvider.provideRoomDatabase(application)

        setUpRecyclerView()

        viewModel = ViewModelProviders.of(
            this,
            FeatureViewModelFactory(FeatureRepositoryImpl(Webservices.instance))
        )
            .get(FeatureViewModel::class.java)
        viewModel.items.observe(this, Observer {
            itemAdapter.featureList.clear()
            itemAdapter.featureList.addAll(it)
            database.featureDAO().insertAll(it)
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

        viewModel.getAll()

        btnRetry.setOnClickListener {
            viewModel.getAll()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.getAll()
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
}
