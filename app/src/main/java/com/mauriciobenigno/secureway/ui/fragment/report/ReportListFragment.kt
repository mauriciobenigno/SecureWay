package com.mauriciobenigno.secureway.ui.fragment.report

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity.Companion.REQUEST_REPORT_EDIT
import com.mauriciobenigno.secureway.ui.activity.autenticacao.AutenticacaoActivity
import com.mauriciobenigno.secureway.ui.activity.report.ReportActivity
import com.mauriciobenigno.secureway.ui.adapter.ReportListAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

/*
Créditos icones por https://www.flaticon.com/br/autores/kiranshastry"v
>*/
class ReportListFragment : Fragment() {

    companion object {
        fun newInstance() = ReportListFragment()
    }

    private lateinit var viewModel: ReportViewModel

    private var recyclerView: RecyclerView? = null

    private var txtListaVazia: TextView? = null

    private var adapter: ReportListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_list_fragment, container, false)
        txtListaVazia = rootView.findViewById(R.id.txt_lista_vazia)
        recyclerView = rootView.findViewById(R.id.recyclerListReports)

        recyclerView?.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        configurarAdapter()
    }

    fun configurarAdapter(){
        doAsync {

            val listaReportZona = mutableListOf<ReportZona>()
            val listaReport = viewModel.getAllReports()
            listaReport.forEach { report ->
                val zona = viewModel.getZonaById(report.id_zona)
                if(zona != null){
                    val endereco = Geocoder(requireActivity().applicationContext).getFromLocation(zona.coordenada_x,zona.coordenada_y, 1)[0]

                    if(endereco != null)
                        listaReportZona.add(ReportZona(report,zona, endereco))
                    else
                        listaReportZona.add(ReportZona(report,zona))
                }

            }

            runOnUiThread {

                if(listaReport.isEmpty()){
                    txtListaVazia?.let {
                        it.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                    }
                }
                else {
                    txtListaVazia?.let {
                        it.visibility = View.GONE
                        recyclerView?.visibility = View.VISIBLE
                    }

                    adapter = ReportListAdapter(listaReportZona!!){ reportZona ->
                        val intent = Intent(requireContext(), ReportActivity::class.java)
                        intent.putExtra("tela_avancada", true)
                        intent.putExtra("report", reportZona)
                        requireActivity().startActivityForResult(intent, REQUEST_REPORT_EDIT)
                    }

                    recyclerView?.adapter = adapter
                }
            }
        }
    }

}