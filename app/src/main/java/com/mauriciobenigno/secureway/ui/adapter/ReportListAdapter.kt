package com.mauriciobenigno.secureway.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona


class ReportListAdapter(private var list: List<ReportZona>, private val listener: (ReportZona) -> Unit) :
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

        viewHolder.itemView.setOnClickListener { listener(item) }

        viewHolder.tvTitulo.setText("Zona id = ${item.report.id_zona}")
        viewHolder.tvAvaliacao.setText("Avaliação: ${item.report.densidade} ")
        viewHolder.tvPontucao.setText("Pontos: ${item.zona.densidade}")

    }

    override fun getItemCount() = list.size
}
