package com.tomato.weather.db.dao

import androidx.room.*
import com.tomato.weather.db.entity.CacheEntity

@Dao
interface CacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache: CacheEntity): Long

    @Query("select *from cache where `key`=:key")
    fun getCache(key: String): CacheEntity?

    @Delete
    fun deleteCache(cache: CacheEntity): Int

}