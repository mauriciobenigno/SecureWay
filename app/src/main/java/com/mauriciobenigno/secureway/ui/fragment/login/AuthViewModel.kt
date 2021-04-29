package com.mauriciobenigno.secureway.ui.fragment.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.repository.AppRepository
import java.util.concurrent.BlockingQueue

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)

    fun getReportsByUser(numero: String) {
        appRepository.getReportsByUser(numero)
    }

    fun syncFetchAdjetivosFromServer() {
        appRepository.syncFetchAdjetivosFromServer()
    }

    fun syncFetchZonasFromServer() {
        appRepository.syncFetchZonasFromServer()
    }

}