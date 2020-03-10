package com.example.headspacechallenge.data.repository

import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.data.remote.Webservices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FeatureRepositoryImpl (private val webservices: Webservices) : FeatureRepository {
    override fun getAll(): Single<List<FeatureModel>> {
        return webservices.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}