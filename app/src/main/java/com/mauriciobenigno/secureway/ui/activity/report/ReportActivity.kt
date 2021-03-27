package com.mauriciobenigno.secureway.ui.activity.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.ReportZona
import com.mauriciobenigno.secureway.ui.fragment.login.ConfirmaAutenticacaoFragment
import com.mauriciobenigno.secureway.ui.fragment.report.ReportActionsFragment
import com.mauriciobenigno.secureway.ui.fragment.report.ReportFragment

class ReportActivity : AppCompatActivity() , OnCommunicateReportInterface {

    private var fragmentAtual: Fragment? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Reportar"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener {
            this.onBackPressed()
        }
        val bundle = Bundle()
        bundle.putAll( intent.extras)


        val telaAvancada = bundle.getBoolean("tela_avancada", false)

        if(telaAvancada){
            val novoFragment: Fragment = ReportActionsFragment()
            novoFragment.arguments = bundle
            fragmentAtual = novoFragment
        }
        else {
            val novoFragment: Fragment = ReportFragment()
            novoFragment.arguments = bundle
            fragmentAtual = novoFragment
        }
    }

    override fun onResume() {
        super.onResume()
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame_report, fragmentAtual!!)
            ft.commit()
        }
    }

    override fun onClickEdit(reportZona: ReportZona?) {

        val novoFragment: Fragment = ReportFragment()
        val bundle = Bundle()
        bundle.putAll(intent.extras)
        novoFragment.arguments = bundle

        fragmentAtual = novoFragment
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container_frame_report, fragmentAtual!!)
        ft.commit()
    }
}

interface OnCommunicateReportInterface {
    fun onClickEdit(reportZona: ReportZona?)
}