package com.mauriciobenigno.secureway.ui.fragment.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.repository.AppRepository
import java.util.concurrent.BlockingQueue

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)

    init {
        appRepository.fetchAdjetivoFromServer()
    }

    fun refreshData() {
        appRepository.fetchAdjetivoFromServer()
    }

    fun getAllAdjetivosFiltrado(posicao: Boolean) = appRepository.getAllAdjetivosFiltrado(posicao)

    fun getAllAdjetivosPositivos() = appRepository.getAllAdjetivosPositivos()

    fun getAllAdjetivosNegativos() = appRepository.getAllAdjetivosNegativos()


    fun getZonaByLocation(coordenada: Coordenada): BlockingQueue<Zona> {
        return appRepository.getZonaByLocation(coordenada)
    }

    fun saveZonaOnServer(zona: Zona) {
        appRepository.saveZonaOnServer(zona)
    }

    fun saveReportOnServer(report: Pair<Report, Coordenada>) {
        appRepository.saveReportOnServer(report)
    }

}