package com.tomato.weather.db.dao

import androidx.room.*
import com.tomato.weather.db.entity.CityEntity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCity(city: CityEntity): Long

    @Query("select * from city")
    fun getCities(): List<CityEntity>

    @Query("delete from city where cityId=:id")
    fun removeCity(id: String)

    @Query("delete from city")
    fun removeAllCity()

}