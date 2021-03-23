package com.mauriciobenigno.secureway.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Adjetivo


class AdjetivoAdapter(private var list: List<Pair<Adjetivo, Adjetivo>>) :
    RecyclerView.Adapter<AdjetivoAdapter.ViewHolder>() {

    private val listChecked = mutableListOf<Adjetivo>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rgOpcoes: RadioGroup
        val rbPositive: RadioButton
        val rbNegative: RadioButton


        val imgViewPositive: ImageView
        val imgViewNegative: ImageView

        val llPositive: LinearLayout
        val llNegative: LinearLayout

        init {
            rgOpcoes = view.findViewById(R.id.rgOpcoes)
            rbPositive = view.findViewById(R.id.rbPositive)
            rbNegative = view.findViewById(R.id.rbNegative)

            imgViewPositive = view.findViewById(R.id.img_positive)
            imgViewNegative = view.findViewById(R.id.img_negative)

            llPositive = view.findViewById(R.id.ll_positive)
            llNegative = view.findViewById(R.id.ll_negative)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_adjetivo, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val adjetivoPositivo = list[position].first
        val adjetivoNegativo = list[position].second

        //viewHolder.imgViewPositive.setBackgroundResource(R.drawable.ic_emoji_positivo)
        //viewHolder.imgViewNegative.setBackgroundResource(R.drawable.ic_emoji_negativo)
        viewHolder.imgViewPositive.setImageResource(R.drawable.ic_emoji_positivo)
        viewHolder.imgViewNegative.setImageResource(R.drawable.ic_emoji_negativo)

        viewHolder.rbPositive.setText(adjetivoPositivo.descricao)
        viewHolder.rbNegative.setText(adjetivoNegativo.descricao)

        viewHolder.llPositive.setOnClickListener {
            marcar(viewHolder, true)
        }

        viewHolder.llNegative.setOnClickListener {
            marcar(viewHolder, false)
        }

        viewHolder.rbPositive.setOnClickListener {
            marcar(viewHolder, true)
        }

        viewHolder.rbNegative.setOnClickListener {
            marcar(viewHolder, false)
        }

    }

    private fun marcar(viewHolder: ViewHolder, opcao: Boolean, items: Pair<Adjetivo, Adjetivo>) {
        if(opcao){
            viewHolder.rbPositive.isChecked = true
            viewHolder.rbNegative.isChecked = false
            listChecked.add(items.first)
            listChecked.remove(items.second)
        } else {
            viewHolder.rbPositive.isChecked = false
            viewHolder.rbNegative.isChecked = true
            listChecked.add(items.second)
            listChecked.remove(items.first)
        }
    }

    override fun getItemCount() = list.size

    fun getAllCheckedList() = listChecked

    fun allOptionsVerifyed() : Boolean {
        return listChecked.size >= (list.size/2)
    }

    fun setList(list: List<Pair<Adjetivo, Adjetivo>>){
        this.list = list
    }
}
