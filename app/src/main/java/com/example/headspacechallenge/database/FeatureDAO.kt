package com.example.headspacechallenge.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.headspacechallenge.data.model.FeatureModel
import io.reactivex.Maybe

@Dao
interface FeatureDAO {
    @Query("SELECT * FROM feature")
    fun getAll(): List<FeatureModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAll(models: List<FeatureModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(featureModel: FeatureModel)
}