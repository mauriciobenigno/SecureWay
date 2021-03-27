package com.mauriciobenigno.secureway.ui.adapter

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.util.ScoreUtils


class ReportListAdapter(private var list: List<ReportZona>, private val listener: (ReportZona) -> Unit) :
    RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvReport: TextView
        val tvLocal: TextView
        val tvAvaliacao: TextView
        val tvPontucao: TextView

        init {
            tvReport = view.findViewById(R.id.tv_zona)
            tvLocal = view.findViewById(R.id.tv_local)
            tvAvaliacao = view.findViewById(R.id.tv_avaliacao)
            tvPontucao = view.findViewById(R.id.tv_pontuacao)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_report_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = list[position]

        viewHolder.itemView.setOnClickListener { listener(item) }

        viewHolder.tvReport.setText("Report ${item.report.id_report} / Zona ${item.report.id_zona}")
        if(item.endereco != null){
            viewHolder.tvLocal.setText("${getPrimeiraDescricao(item.endereco!!)}")
        } else {
            viewHolder.tvLocal.setText("Endereço indisponível")
        }

        viewHolder.tvAvaliacao.setText("Minha Avaliação: ${ScoreUtils.obterAvaliacao(item.report.densidade)} ")
        viewHolder.tvPontucao.setText("Avaliação geral: ${ScoreUtils.obterAvaliacao(item.zona.densidade)}")

    }

    override fun getItemCount() = list.size

    private fun getPrimeiraDescricao(endereco: Address): String {
        var descricao = "Local: ";
        descricao += if (endereco.subLocality != null)
            endereco.subLocality;
        else if (endereco.locality != null)
            endereco.locality;
        else if (endereco.adminArea != null && endereco.adminArea.length >= 2)
            endereco.adminArea;
        else if (endereco.postalCode != null && endereco.postalCode.isNotEmpty())
            endereco.postalCode;
        else
            " Desconhecido"
        return descricao
    }
}
