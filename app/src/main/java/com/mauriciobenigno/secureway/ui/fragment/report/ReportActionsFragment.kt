package com.mauriciobenigno.secureway.ui.fragment.report

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.ui.activity.report.OnCommunicateReportInterface
import com.mauriciobenigno.secureway.util.ScoreUtils
import java.text.SimpleDateFormat
import java.util.*


/*
Créditos icones por https://www.flaticon.com/br/autores/kiranshastry"v
>*/
class ReportActionsFragment : Fragment() {

    companion object {
        fun newInstance() = ReportActionsFragment()
    }

    private lateinit var viewModel: ReportViewModel

    var onCommunicate: OnCommunicateReportInterface? = null


    private var reportZona: ReportZona? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.report_actions_fragment, container, false)

        val bundle = arguments

        if (bundle != null) {
            reportZona = bundle.get("report") as ReportZona
        }


        val tvZonaAcao = view.findViewById(R.id.tv_zona_acao) as TextView
        val tvZonaStatus = view.findViewById(R.id.tv_zona_resultado) as TextView
        val tvDataReport = view.findViewById(R.id.tv_data_report) as TextView
        val tvPontoReport = view.findViewById(R.id.tv_pontuacao_report) as TextView
        val tvPontoZona = view.findViewById(R.id.tv_pontuacao_zona) as TextView

        val tvCEP = view.findViewById(R.id.tv_endereco_zona_CEP) as TextView
        val tvRua = view.findViewById(R.id.tv_endereco_zona_rua) as TextView
        val tvBairro = view.findViewById(R.id.tv_endereco_zona_bairro) as TextView
        val tvCidade = view.findViewById(R.id.tv_endereco_zona_cidade) as TextView
        val tvEstado = view.findViewById(R.id.tv_endereco_zona_estado) as TextView

        val imgMapPreview = view.findViewById(R.id.img_map_preview) as ImageView

        val btnAlterarReport = view.findViewById(R.id.btnAlterarReport) as Button


        reportZona?.let {
            tvZonaAcao.setText("Zona ID ${it.report.id_zona} / Report ID ${it.report.id_report}")
            tvZonaStatus.setText(ScoreUtils.obterAvaliacao(it.zona.densidade))
            tvDataReport.setText("Data: ${it.report.data_report}")
            tvPontoReport.setText("Pessoal: ${ScoreUtils.obterAvaliacao(it.report.densidade)}")
            tvPontoZona.setText("Geral: ${ScoreUtils.obterAvaliacao(it.zona.densidade)}")
        }

        if(reportZona!= null){
            reportZona!!.zona?.let {
                if(it.densidade <= 400){
                    imgMapPreview.setImageResource(R.drawable.ic_emoji_positivo)
                } else {
                    imgMapPreview.setImageResource(R.drawable.ic_emoji_negativo)
                }

                val endereco = Geocoder(requireActivity().applicationContext).getFromLocation(it.coordenada_x,it.coordenada_y, 1)[0]
                if (endereco.getThoroughfare() != null) {
                    tvRua.setText(Html.fromHtml("<b>" + "Rua: " + "</b>" + endereco.getThoroughfare()))
                }else{
                    tvRua.visibility = View.GONE
                }

                if (endereco.getSubLocality() != null) {
                    tvBairro.setText(Html.fromHtml("<b>" + "Bairro: " + "</b>" + endereco.getSubLocality()))
                }else{
                    tvBairro.visibility = View.GONE
                }
                
                if (endereco.getLocality() != null) {
                    tvCidade.setText(Html.fromHtml("<b>" + "Município: " + "</b>" + endereco.getLocality()))
                }else{
                    tvCidade.visibility = View.GONE
                }

                if (endereco.getAdminArea() != null && endereco!!.getAdminArea().length >= 2) {
                    tvEstado.setText(Html.fromHtml("<b>" + "Estado: " + "</b>" + endereco.getAdminArea()))
                }else{
                    tvEstado.visibility = View.GONE
                }

                if (endereco.getPostalCode() != null && endereco!!.getPostalCode().isNotEmpty()) {
                    tvCEP.setText(Html.fromHtml("<b>" + "CEP: " + "</b>" + endereco.getPostalCode()))
                }else{
                    tvCEP.visibility = View.GONE
                }
            }
        }


        btnAlterarReport.setOnClickListener {
            onCommunicate?.onClickEdit(reportZona)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onCommunicate = context as OnCommunicateReportInterface
        } catch (e: Exception) {
            //Log.e("onAttach", e.toString())
        }
    }
}