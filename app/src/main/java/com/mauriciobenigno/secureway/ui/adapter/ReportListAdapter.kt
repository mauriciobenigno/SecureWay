package com.mauriciobenigno.secureway.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Report
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.ui.fragment.report.ReportViewModel
import org.jetbrains.anko.doAsync


class ReportListAdapter(private var list: List<ReportZona>) :
    RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTitulo: TextView
        val tvAvaliacao: TextView
        val tvPontucao: TextView

        init {
            tvTitulo = view.findViewById(R.id.tv_zona)
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

        viewHolder.tvTitulo.setText("Zona id = ${item.report.id_zona}")
        viewHolder.tvAvaliacao.setText("Avaliação: ${item.report.densidade} ")
        viewHolder.tvPontucao.setText("Pontos: ${item.zona.densidade}")

    }

    override fun getItemCount() = list.size

}
