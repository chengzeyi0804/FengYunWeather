package com.tomato.weather.ui.activity.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tomato.weather.db.AppRepo
import com.tomato.weather.db.entity.CityEntity
import com.tomato.weather.ui.base.BaseViewModel

class CityManagerViewModel(private val app: Application) : BaseViewModel(app) {

    val cities = MutableLiveData<List<CityEntity>>()

    fun getCities() {
        launch {
            val results = AppRepo.getInstance().getCities()
            cities.postValue(results)
        }
    }

    fun removeCity(cityId: String) {
        launch {
            AppRepo.getInstance().removeCity(cityId)
        }
    }

    fun updateCities(it: List<CityEntity>) {
        launch {
            AppRepo.getInstance().removeAllCity()
            it.forEach {
                AppRepo.getInstance().addCity(it)
            }
        }
    }
}