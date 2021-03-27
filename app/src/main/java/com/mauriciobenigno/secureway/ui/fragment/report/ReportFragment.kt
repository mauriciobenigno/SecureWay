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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Adjetivo
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.ui.adapter.AdjetivoAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread
import java.util.*

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

                /*
                * Sempre que a pontuação for ruim, vai juntar 200
                * Até o momento a pontuação máxima é 1000, caso hajam mais advetivos no server, a pontuação máxima irá aumentar dinamicamente
                *
                * */

                var pontuacao: Double = 0.0

                adapter!!.getAllCheckedList().forEach {
                    if(it.negativo == 1)
                        pontuacao+=200
                }

                val sequencia = adapter!!.getCheckedSequence()

                try {
                    if(Firebase.auth.currentUser != null){

                        var numeroString = Firebase.auth.currentUser!!.phoneNumber!!
                        numeroString = numeroString.replace("+","")
                        val numeroLong = numeroString.toLong()

                        val report = Report(0, 0,numeroLong, Date().toString(),pontuacao, sequencia)
                        viewModel.saveReportOnServer(Pair(report,Coordenada(endereco!!.latitude,endereco!!.longitude)))

                        Toast.makeText(requireContext(), "Opinião registrada",Toast.LENGTH_LONG).show()
                        requireActivity().finish()

                    } else {
                        Toast.makeText(requireContext(), "Você não está logado!",Toast.LENGTH_LONG).show()
                        //requireActivity().finish()
                    }
                }
                catch (e: Exception){
                    // Erro, futuramente colocar tela de erro
                }



            } else {
                Toast.makeText(requireContext(), "Marque uma opção em cada dupla",Toast.LENGTH_LONG).show()
            }

        }

    }

}