package com.mauriciobenigno.secureway.ui.fragment.notice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.common.internal.Constants
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.repository.AppRepository
import java.util.ArrayList
import java.util.HashMap

class NoticeViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)


    var dangerZoneList: LiveData<List<GeoCerca>> =
        Transformations.map(appRepository.getDangerZones(), ::getDangerZones)


    private fun getDangerZones(tolls: List<Zona>): List<GeoCerca> {

        val list = ArrayList<GeoCerca>()

        tolls.forEach {
            list.add(
                GeoCerca(
                    it.id_zona.toString(),
                    it.coordenada_x,
                    it.coordenada_y,
                    5.0
                )
            )
        }
        return list
    }

    fun getDangerZonesBackground() = appRepository.getDangerZonesBackground().map { GeoCerca(it.id_zona.toString(), it.coordenada_x, it.coordenada_y, 5.0)}
}