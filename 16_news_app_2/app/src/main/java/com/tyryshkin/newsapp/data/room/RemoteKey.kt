package com.tyryshkin.newsapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
