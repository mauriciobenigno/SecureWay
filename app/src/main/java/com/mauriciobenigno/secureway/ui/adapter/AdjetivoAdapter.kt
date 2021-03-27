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
   // private var listMarcadosString = mutableListOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rgOpcoes: RadioGroup
        val rbPositive: RadioButton
        val rbNegative: RadioButton

        val tvTitulo: TextView
        val imgViewPositive: ImageView
        val imgViewNegative: ImageView

        val llPositive: LinearLayout
        val llNegative: LinearLayout

        init {
            rgOpcoes = view.findViewById(R.id.rgOpcoes)
            rbPositive = view.findViewById(R.id.rbPositive)
            rbNegative = view.findViewById(R.id.rbNegative)

            tvTitulo = view.findViewById(R.id.tv_titulo)

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

        /*if(listMarcadosString.size >= position + 1){
            if(listMarcadosString[position] == "0"){
                marcar(viewHolder, true, list[position])
            } else if (listMarcadosString[position] == "1"){
                marcar(viewHolder, false, list[position])
            }
        }*/

        viewHolder.tvTitulo.setText(adjetivoPositivo.descricao)

        viewHolder.imgViewPositive.setImageResource(R.drawable.ic_emoji_positivo)
        viewHolder.imgViewNegative.setImageResource(R.drawable.ic_emoji_negativo)

        viewHolder.llPositive.setOnClickListener {
            marcar(viewHolder, true, list[position])
        }

        viewHolder.llNegative.setOnClickListener {
            marcar(viewHolder, false, list[position])
        }

        viewHolder.rbPositive.setOnClickListener {
            marcar(viewHolder, true, list[position])
        }

        viewHolder.rbNegative.setOnClickListener {
            marcar(viewHolder, false, list[position])
        }
    }

    private fun marcar(viewHolder: ViewHolder, opcao: Boolean, items: Pair<Adjetivo, Adjetivo>) {
        if(opcao){
            viewHolder.rbPositive.isChecked = true
            viewHolder.rbNegative.isChecked = false

            listChecked.remove(items.second)
            listChecked.add(items.first)
        } else {
            viewHolder.rbPositive.isChecked = false
            viewHolder.rbNegative.isChecked = true

            listChecked.remove(items.first)
            listChecked.add(items.second)
        }
    }

    fun getCheckedSequence() : String{
        var sequencia = ""

        for(i in 0 until  list.size){
            if(list.get(i).first.equals(listChecked.get(i))){
                sequencia+="0,"
            } else if (list.get(i).second.equals(listChecked.get(i))){
                sequencia+="1,"
            }
        }

        if(!sequencia.isEmpty())
            sequencia = sequencia.subSequence(0,sequencia.length-1) as String
       return sequencia
    }

    override fun getItemCount() = list.size

    fun getAllCheckedList() = listChecked

    fun allOptionsVerifyed() : Boolean {
        return listChecked.size >= list.size
    }

    fun getCountChecked() = listChecked.size

    fun setList(list: List<Pair<Adjetivo, Adjetivo>>){
        this.list = list
    }

    /*fun setListMarcados(list: MutableList<String>){
        this.listMarcadosString = list
    }*/

}
