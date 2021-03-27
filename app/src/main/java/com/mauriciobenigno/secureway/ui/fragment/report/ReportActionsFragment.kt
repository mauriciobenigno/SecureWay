package com.mauriciobenigno.secureway.ui.fragment.report

import android.location.Address
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.ui.adapter.ReportListAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread

/*
Créditos icones por https://www.flaticon.com/br/autores/kiranshastry"v
>*/
class ReportActionsFragment : Fragment() {

    companion object {
        fun newInstance() = ReportActionsFragment()
    }

    private lateinit var viewModel: ReportViewModel

    private var recyclerView: RecyclerView? = null

    private var adapter: ReportListAdapter? = null

    private var endereco: Address? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.report_actions_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerListReports)

        recyclerView?.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        val bundle = arguments

        if(bundle!= null){
            endereco = bundle.get("endereco") as Address
        }


        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        doAsync {

            val listaReportZona = mutableListOf<ReportZona>()
            val listaReport = viewModel.get()

            runOnUiThread {
                adapter = ReportListAdapter(listaReport!!)
                recyclerView?.adapter = adapter
            }


        }

    }

}