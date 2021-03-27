package com.mauriciobenigno.secureway.ui.fragment.report

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.ui.activity.report.OnCommunicateReportInterface


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

        if(bundle!= null){
            reportZona = bundle.get("report") as ReportZona
        }

        val tvZonaAcao = view.findViewById(R.id.tv_zona_acao) as TextView
        val tvDataReport = view.findViewById(R.id.tv_data_report) as TextView
        val tvPontoReport = view.findViewById(R.id.tv_pontuacao_report) as TextView
        val tvPontoZona = view.findViewById(R.id.tv_pontuacao_zona) as TextView

        val imgMapPreview = view.findViewById(R.id.img_map_preview) as ImageView

        val btnEditarReport = view.findViewById(R.id.btnEditarReport) as Button



        reportZona?.let {

            /*Glide.with(this)
                .load(Uri.decode(getGoogleImageURL("", it.zona.coordenada_x, it.zona.coordenada_y))) // image url
                .placeholder(R.drawable.ic_emoji_positivo) // any placeholder to load at start
                .error(R.drawable.ic_emoji_negativo)  // any image in case of error
                .override(200, 200)// resizing
                .centerCrop()
                .into(imgMapPreview);  // imageview object
*/
            tvZonaAcao.setText("Zona ID: ${it.report.id_zona}")
            tvDataReport.setText("Data: ${it.report.data_report}")
            tvPontoReport.setText("Ponto Zona: ${it.report.densidade}")
            tvPontoZona.setText("Ponto Report: ${it.zona.densidade}")
        }

        btnEditarReport.setOnClickListener {
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

    fun getGoogleImageURL(key: String,lati: Double, longi: Double): String {
        return "http://maps.google.com/maps/api/staticmap?center=$lati%2C$longi&zoom=16&format=png&maptype=roadmap&mobile=false&markers=|color:%23128DD9|label:Marker|$lati%2C$longi&size=1000x400&key=$key&sensor=false"
    }

}