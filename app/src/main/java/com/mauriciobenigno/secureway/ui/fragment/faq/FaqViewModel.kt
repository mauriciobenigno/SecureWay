package com.mauriciobenigno.secureway.ui.fragment.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Faq
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.repository.AppRepository
import java.util.ArrayList
import java.util.HashMap

class FaqViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository = AppRepository(application)

    init {
        appRepository.fetchFaqFromServer()
    }

    fun getAllFaq() : Pair<List<Faq>, HashMap<Long, List<Faq>>>{
       var listFaq = appRepository.getAllFaq()
       var listFaqAdapter =  HashMap<Long, List<Faq>>()

        listFaq.forEach {
            val listZero = ArrayList<Faq>()
            listZero.add(it)
            listFaqAdapter.put(it.id_faq, listZero)
        }

        return Pair(listFaq,listFaqAdapter)
    }
}