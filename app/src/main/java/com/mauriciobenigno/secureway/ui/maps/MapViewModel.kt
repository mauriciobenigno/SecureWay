package com.mauriciobenigno.secureway.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.repository.AppRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)

    /*init {
        appRepository.fetchDataFromServer()
    }

    fun refreshData() {
        appRepository.fetchDataFromServer()
    }*/


    fun generateHeatMapData(context: Context): ArrayList<WeightedLatLng> {
        return appRepository.generateHeatMapData(context)
    }
}