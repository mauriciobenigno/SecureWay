package com.mauriciobenigno.secureway.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.util.ScoreUtils


class InfoRegiaoAdapter(context: Context?) : GoogleMap.InfoWindowAdapter {

    private var mWindow: View = (context as Activity).layoutInflater.inflate(R.layout.adapter_info_regiao, null)

    private fun configurarTela(marker: Marker, view: View){

        val imgMapStatus = view.findViewById(R.id.img_map_status) as ImageView
        val tvZonaID = view.findViewById(R.id.tv_zona_id) as TextView
        val tvZonaAvaliacao = view.findViewById(R.id.tv_zona_status) as TextView

        var zona = marker.tag as Zona?
        if(zona != null){
            tvZonaID.text = "Zona ${zona!!.id_zona}"
            tvZonaAvaliacao.text = ScoreUtils.obterAvaliacao(zona!!.densidade)

            if(zona!!.densidade <= 400){
                imgMapStatus.setImageResource(R.drawable.ic_emoji_positivo)
            } else {
                imgMapStatus.setImageResource(R.drawable.ic_emoji_negativo)
            }
        }
    }

    override fun getInfoWindow(marker: Marker?): View {
        configurarTela(marker!!, mWindow)
        return mWindow
    }

    override fun getInfoContents(marker: Marker?): View {
        configurarTela(marker!!, mWindow)
        return mWindow
    }

}
