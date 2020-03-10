package com.example.headspacechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feature")
data class FeatureModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "download_url") val download_url: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "width") val width: Int
)