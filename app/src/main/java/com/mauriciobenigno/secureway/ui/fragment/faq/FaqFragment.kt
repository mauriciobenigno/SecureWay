package com.mauriciobenigno.secureway.ui.fragment.faq

import android.app.Activity
import android.app.ProgressDialog
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.ui.adapter.AdjetivoAdapter
import com.mauriciobenigno.secureway.ui.adapter.FaqAdapter
import com.mauriciobenigno.secureway.util.DateUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.util.*


class FaqFragment : Fragment() {

    companion object {
        fun newInstance() = FaqFragment()
    }

    private lateinit var viewModel: FaqViewModel

    private var expandableListView: ExpandableListView? = null

    private var adapter: FaqAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_fragment, container, false)


        expandableListView = rootView.findViewById(R.id.listFaq)


        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FaqViewModel::class.java)

        doAsync {
            val listTitulos = viewModel.getTitulosFAQ()
            val listConteudo = viewModel.getConteudoFAQ()

            runOnUiThread {
                adapter = FaqAdapter(listTitulos, listConteudo)
                /*if( listaMarcados.size > 0)
                    adapter!!.setListMarcados(listaMarcados)*/
                expandableListView?.adapter = adapter
            }
        }


    }

}