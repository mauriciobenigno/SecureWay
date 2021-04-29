package com.mauriciobenigno.secureway.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.repository.AppRepository


class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)

    fun getZoneDataFromServer(context: Context) {
        appRepository.fetchZonasByLocationFromServer(context)
    }

    fun getLiveAllDistrict() = appRepository.getLiveAllDistric()

    fun getHeatMapData(): ArrayList<WeightedLatLng> {
        return appRepository.getHeatMapData()
    }

    /*fun saveHeapPoint(district: District) {
        appRepository.saveSaveOnServer(district)
    }*/

    fun saveZonaOnServer(zona: Zona) {
        appRepository.saveZonaOnServer(zona)
    }

    fun getZonaByLocation(coordenada: Coordenada): Zona? {
        return appRepository.getZonaByLocation(coordenada)
    }


}