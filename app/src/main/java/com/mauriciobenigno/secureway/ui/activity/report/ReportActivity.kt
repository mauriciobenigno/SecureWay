package com.mauriciobenigno.secureway.ui.activity.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.fragment.report.ReportFragment

class ReportActivity : AppCompatActivity() {

    private var fragmentAtual: Fragment? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Reportar"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fragmentAtual = ReportFragment()
    }

    override fun onResume() {
        super.onResume()
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame_report, fragmentAtual!!)
            ft.commit()
        }
    }
}