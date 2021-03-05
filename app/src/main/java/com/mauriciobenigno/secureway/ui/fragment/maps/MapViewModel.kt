package com.mauriciobenigno.secureway.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.model.District
import com.mauriciobenigno.secureway.repository.AppRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)

    init {
        appRepository.fetchLocationsFromServer()
    }

    fun refreshData() {
        appRepository.fetchLocationsFromServer()
    }

    fun getLiveAllDistrict() = appRepository.getLiveAllDistric()

    fun getHeatMapData(): ArrayList<WeightedLatLng> {
        return appRepository.getHeatMapData()
    }

    fun saveHeapPoint(district: District) {
        appRepository.saveSaveOnServer(district)
    }
}