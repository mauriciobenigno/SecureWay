package com.mauriciobenigno.secureway.ui.fragment.faq

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.ui.adapter.FaqAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.lang.Exception
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
        val rootView: View = inflater.inflate(R.layout.faq_list_fragment, container, false)

        expandableListView = rootView.findViewById(R.id.listFaq)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FaqViewModel::class.java)

        doAsync {
            val parList = viewModel.getAllFaq()

            runOnUiThread {
                //adapter = FaqAdapter(listTitulos, listConteudo)
                adapter = FaqAdapter(requireContext(),parList.first, parList.second)
                /*if( listaMarcados.size > 0)
                    adapter!!.setListMarcados(listaMarcados)*/

                try {
                    expandableListView?.setAdapter(adapter)
                }
                catch (e: Exception){
                    e?.message?.let {
                        Log.e("teste", it)
                    }
                }

            }
        }


    }

}