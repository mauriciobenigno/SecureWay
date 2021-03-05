package com.mauriciobenigno.secureway.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Adjetivo


class AdjetivoAdapter(private val list: List<Adjetivo>) :
    RecyclerView.Adapter<AdjetivoAdapter.ViewHolder>() {

    private val listChecked = mutableListOf<Adjetivo>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbItem: CheckBox

        init {
            cbItem = view.findViewById(R.id.cbItem)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_adjetivo, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.cbItem.text = list[position].descricao
        viewHolder.cbItem.setOnCheckedChangeListener { compoundButton, isChecked  ->
            if(isChecked){
                listChecked.add(list[position])
            } else {
                listChecked.remove(list[position])

            }

        }
    }

    override fun getItemCount() = list.size

    fun getAllCheckedList() = listChecked
}
