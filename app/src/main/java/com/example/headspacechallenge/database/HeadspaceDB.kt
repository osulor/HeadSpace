package com.example.headspacechallenge.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.headspacechallenge.data.model.FeatureModel


const val DATABASE_NAME = "headspace-database"

object DatabaseProvider {
    fun provideRoomDatabase(application: Application): HeadspaceDB {
        return Room.databaseBuilder(application, HeadspaceDB::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

@Database(entities = [FeatureModel::class], version = 1)
abstract class HeadspaceDB: RoomDatabase(){
    abstract fun featureDAO(): FeatureDAO
}