package com.mauriciobenigno.secureway.ui.fragment.report

import android.location.Address
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Adjetivo
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.ui.adapter.AdjetivoAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread

/*
Créditos icones por https://www.flaticon.com/br/autores/kiranshastry"v
>*/
class ReportFragment : Fragment() {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private lateinit var viewModel: ReportViewModel

    private var btnGravarReport: Button? = null

    private var recyclerView: RecyclerView? = null

    private var adapter: AdjetivoAdapter? = null

    private var endereco: Address? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_fragment, container, false)

        btnGravarReport = rootView.find(R.id.btnGravarReport)

        recyclerView = rootView.findViewById(R.id.recyclerListAdjetivos)


        val bundle = arguments

        if(bundle!= null){
            endereco = bundle.get("endereco") as Address
        }


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
                adapter = AdjetivoAdapter(lista)
                recyclerView?.adapter = adapter
            }
        }

        btnGravarReport?.setOnClickListener {
            if(adapter?.allOptionsVerifyed() == true){
                viewModel.saveZonaOnServer(Zona(0,endereco!!.latitude,endereco!!.longitude,500.0))
                Toast.makeText(requireContext(), "Opinião registrada",Toast.LENGTH_LONG).show()
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Marque uma opção em cada dupla",Toast.LENGTH_LONG).show()
            }

        }

    }

}