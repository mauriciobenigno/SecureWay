package com.mauriciobenigno.secureway.ui.fragment.report

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Adjetivo
import com.mauriciobenigno.secureway.ui.adapter.AdjetivoAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

/*
Créditos icones por https://www.flaticon.com/br/autores/kiranshastry"v
>*/
class ReportFragment : Fragment() {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private lateinit var viewModel: ReportViewModel

    private var ll_positivo: LinearLayout? = null
    private var ll_negativo: LinearLayout? = null

    private var ib_positivo: ImageButton? = null
    private var ib_negativo: ImageButton? = null

    private var recyclerView: RecyclerView? = null

    private var posicao: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_fragment, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerListAdjetivos)

       // recyclerView?.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        /*
        lista.add(Adjetivo(1,true, "teste 1"))
        lista.add(Adjetivo(2,true, "teste 3"))
        lista.add(Adjetivo(3,true, "teste sdsad"))
        lista.add(Adjetivo(4,true, "teste dfdf"))
        lista.add(Adjetivo(5,true, "teste dsdfsf"))
        lista.add(Adjetivo(6,true, "teste qwqwqw"))

        recyclerView?.setAdapter(AdjetivoAdapter(lista))*/

        doAsync {
            val listPositive = viewModel.getAllAdjetivosPositivos()
            val listNegative = viewModel.getAllAdjetivosNegativos()

            val lista = mutableListOf<Pair<Adjetivo, Adjetivo>>()

            var count = 0
            for(item in listPositive){
                lista.add(Pair(item,listNegative.get(count)))
                count = count+1
            }

            runOnUiThread {
                recyclerView?.adapter = AdjetivoAdapter(lista)
            }
        }

    }

}