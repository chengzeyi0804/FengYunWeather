package com.tomato.lib.logdb.dao

import androidx.room.*
import com.tomato.lib.logdb.entity.LogEntity

@Dao
interface LogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLog(log: LogEntity): Long

    @Query("select * from log")
    fun getAll(): List<LogEntity>

    @Query("delete from log")
    fun deleteAll()

}