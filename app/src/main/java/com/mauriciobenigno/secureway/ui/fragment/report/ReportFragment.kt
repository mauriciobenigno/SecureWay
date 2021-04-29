package com.mauriciobenigno.secureway.ui.fragment.report

import android.app.Activity
import android.app.ProgressDialog
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.ui.adapter.AdjetivoAdapter
import com.mauriciobenigno.secureway.util.DateUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
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

    private var reportZona: ReportZona? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_fragment, container, false)

        btnGravarReport = rootView.find(R.id.btnGravarReport)

        recyclerView = rootView.findViewById(R.id.recyclerListAdjetivos)


        val bundle = arguments

        if(bundle!= null){
            try {
                endereco = bundle.get("endereco") as Address
            }
            catch (e: Exception){
                endereco = null
            }

            try {
                reportZona = bundle.get("report") as ReportZona
                if (endereco == null){
                    endereco = Geocoder(requireActivity().applicationContext).getFromLocation(
                        reportZona?.zona?.coordenada_x!!,reportZona?.zona?.coordenada_y!!, 1)[0]
                }

            }
            catch (e: Exception){
                reportZona = null
            }
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
            var listaMarcados = mutableListOf<String>()

            var count = 0
            for(item in listPositive){
                lista.add(Pair(item,listNegative.get(count)))
                count = count+1
            }

            /*if(reportZona != null){
                listaMarcados = reportZona!!.report.observacao.split(",") as MutableList<String>
            }*/

            runOnUiThread {
                adapter = AdjetivoAdapter(lista)
                /*if( listaMarcados.size > 0)
                    adapter!!.setListMarcados(listaMarcados)*/
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
                        val progressDialog = ProgressDialog(requireContext())
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                        progressDialog.setTitle("Criando reporte")
                        progressDialog.setMessage("Enviando Opinião")
                        progressDialog.setCancelable(false)
                        progressDialog.show()

                        doAsync {
                            var numeroString = Firebase.auth.currentUser!!.phoneNumber!!
                            numeroString = numeroString.replace("+","")
                            val numeroLong = numeroString.toLong()

                            // Se trata de edição
                            if(reportZona!= null){
                                val report = Report(reportZona!!.report.id_report, reportZona!!.report.id_zona,numeroLong, DateUtils.getFormatedDate(Date()),pontuacao, sequencia)
                                viewModel.updateReportOnServer(report)
                            }
                            else {
                                val report = Report(0, 0,numeroLong, DateUtils.getFormatedDate(Date()),pontuacao, sequencia)
                                viewModel.saveReportOnServer(Pair(report,Coordenada(endereco!!.latitude,endereco!!.longitude)))
                            }

                            uiThread {
                                progressDialog.dismiss()
                                Toast.makeText(requireContext(), "Opinião registrada",Toast.LENGTH_LONG).show()
                                requireActivity().setResult(Activity.RESULT_OK)
                                requireActivity().finish()
                            }
                        }


                    } else {
                        Toast.makeText(requireContext(), "Você não está logado!",Toast.LENGTH_LONG).show()
                        //requireActivity().finish()
                    }
                }
                catch (e: Exception){
                    // Erro, futuramente colocar tela de erro
                    val teste = 0
                }



            } else {
                Toast.makeText(requireContext(), "Marque uma opção em cada dupla",Toast.LENGTH_LONG).show()
            }

        }

    }

}