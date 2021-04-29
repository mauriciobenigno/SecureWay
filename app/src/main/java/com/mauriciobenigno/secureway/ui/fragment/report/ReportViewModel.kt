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
        appRepository.fetchZonasFromServer()
    }

    fun refreshData() {
        appRepository.fetchAdjetivoFromServer()
    }

    fun fetchZonasFromServer() {
        appRepository.fetchZonasFromServer()
    }

    fun asyncFetchZonasFromServer() {
        appRepository.asyncFetchZonasFromServer()
    }

    fun getAllAdjetivosFiltrado(posicao: Boolean) = appRepository.getAllAdjetivosFiltrado(posicao)

    fun getAllAdjetivosPositivos() = appRepository.getAllAdjetivosPositivos()

    fun getAllAdjetivosNegativos() = appRepository.getAllAdjetivosNegativos()

    fun getAllReports() = appRepository.getAllReports()

    fun getZonaById(zonaId: Long) = appRepository.getZonaById(zonaId)

    fun saveZonaOnServer(zona: Zona) {
        appRepository.saveZonaOnServer(zona)
    }

    fun saveReportOnServer(report: Pair<Report, Coordenada>) {
        appRepository.saveReportOnServer(report)
    }

    fun updateReportOnServer(report: Report) {
        appRepository.updateReportOnServer(report)
    }

    fun deleteReportOnServer(report: Report) : Boolean {
        return appRepository.deleteReportOnServer(report)
    }



}