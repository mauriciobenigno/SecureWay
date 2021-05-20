package com.mauriciobenigno.secureway.ui.fragment.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.repository.AppRepository

class FaqViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)

    init {
        appRepository.fetchFaqFromServer()
    }

    fun getAllFaq() = appRepository.getAllFaq()
}