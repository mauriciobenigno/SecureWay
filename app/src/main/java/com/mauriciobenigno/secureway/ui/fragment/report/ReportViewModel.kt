package com.mauriciobenigno.secureway.ui.fragment.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mauriciobenigno.secureway.repository.AppRepository

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)

    init {
        appRepository.fetchLocationsFromServer()
    }

    fun refreshData() {
        appRepository.fetchLocationsFromServer()
    }

    fun getAllAdjetivosFiltrado(posicao: Boolean) = appRepository.getAllAdjetivosFiltrado(posicao)

    fun getAllAdjetivosPositivos() = appRepository.getAllAdjetivosPositivos()

    fun getAllAdjetivosNegativos() = appRepository.getAllAdjetivosNegativos()


}