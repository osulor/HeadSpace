package com.example.headspacechallenge.data.repository

import com.example.headspacechallenge.data.model.FeatureModel
import io.reactivex.Single

interface FeatureRepository {
    fun getAll(): Single<List<FeatureModel>>
}