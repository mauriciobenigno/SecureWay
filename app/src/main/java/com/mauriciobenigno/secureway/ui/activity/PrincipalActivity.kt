package com.mauriciobenigno.secureway.ui.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.helper.GeofenceHelper
import com.mauriciobenigno.secureway.ui.MapViewFragment
import com.mauriciobenigno.secureway.ui.activity.autenticacao.AutenticacaoActivity
import com.mauriciobenigno.secureway.ui.fragment.faq.FaqFragment
import com.mauriciobenigno.secureway.ui.fragment.notice.NoticeFragment
import com.mauriciobenigno.secureway.ui.fragment.notice.NoticeViewModel
import com.mauriciobenigno.secureway.ui.fragment.report.ReportListFragment
import com.mauriciobenigno.secureway.util.Preferences
import org.jetbrains.anko.doAsync


class PrincipalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var REQUEST_REPORT_CREATE = 998;
        var REQUEST_REPORT_EDIT = 999;
        private const val TAG = "PrincipalActivity"
    }

    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var fragmentMap: MapViewFragment? = null
    private var fragmentAtual: Fragment? = null

    // Drawer
    private var ll_logado: LinearLayout? = null
    private var ll_deslogado: LinearLayout? = null
    private var tv_drawer_apelido: TextView? = null
    private var tv_drawer_numero: TextView? = null

    // Location
    private var locationProvider: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout!!.addDrawerListener(toggle)

        toggle.syncState()

        navigationView = findViewById(R.id.navView)
        navigationView!!.setNavigationItemSelectedListener(this)

        val header: View = navigationView!!.getHeaderView(0)
        ll_logado = header.findViewById<View>(R.id.ll_drawer_info_logado) as LinearLayout
        ll_deslogado = header.findViewById<View>(R.id.ll_drawer_info_deslogado) as LinearLayout

        ll_deslogado?.setOnClickListener {
            val intent = Intent(this, AutenticacaoActivity::class.java)
            startActivity(intent)
        }

        tv_drawer_apelido = header.findViewById<View>(R.id.tv_drawer_apelido) as TextView
        tv_drawer_numero = header.findViewById<View>(R.id.tv_drawer_numero) as TextView

        // Inicializar Framents
        fragmentMap = MapViewFragment()
        fragmentAtual = fragmentMap


        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }

        // Inicializar GeoFences
        configurarGeoFences()
    }

    private fun configurarContaDrawer(){
        if(Firebase.auth.currentUser != null){
            ll_logado?.visibility  = View.VISIBLE
            ll_deslogado?.visibility  = View.GONE
            tv_drawer_apelido!!.text = Firebase.auth.currentUser!!.displayName
            tv_drawer_numero!!.text = Firebase.auth.currentUser!!.phoneNumber
        } else {
            ll_logado?.visibility  = View.GONE
            ll_deslogado?.visibility  = View.VISIBLE
        }
    }

    @SuppressLint("MissingPermission")
    fun configurarGeoFences(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(Preferences(this).habilitarNotificacoes){
                // remove as cercas anteriores
                desligarGeoFences()
                
                // Inicia o trâmite para adicionar novas cercas
                locationProvider = LocationServices.getFusedLocationProviderClient(this)

                locationProvider?.lastLocation?.addOnSuccessListener { localizacao ->
                    val helper = GeofenceHelper(this)
                    val geofencingClient = LocationServices.getGeofencingClient(this)

                    // Inicializa a ViewModel responsável pelas regiões
                    val viewModel  = ViewModelProvider(this).get(NoticeViewModel::class.java)

                    doAsync {
                        // Prepara lista de GeoCercas
                        val listaIds = ArrayList<String>()

                        // Busca as zonas disponíveis no aparelho e monta as cercas
                        viewModel.getDangerZonesBackground().forEach { geoZona ->
                            val geofence: Geofence = helper.getGeofence(geoZona.id, LatLng(geoZona.lat,geoZona.lng), geoZona.radius.toFloat(), Geofence.GEOFENCE_TRANSITION_ENTER)
                            val geofencingRequest: GeofencingRequest = helper.getGeofencingRequest(geofence)
                            val pendingIntent: PendingIntent = helper.getPendingIntent()
                            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                                .addOnSuccessListener{
                                    listaIds.add(geoZona.id)
                                }
                                .addOnFailureListener{
                                    val errorMessage: String = helper.getErrorString(it)
                                    Log.d(TAG, "onFailure: $errorMessage")
                                }
                        }

                        Preferences(applicationContext).salvarGeoCercaAtual = listaIds
                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun desligarGeoFences(){
        LocationServices.getGeofencingClient(this).removeGeofences(Preferences(this).salvarGeoCercaAtual)
    }

    override fun onResume() {
        super.onResume()
        configurarContaDrawer()

        if(fragmentAtual is ReportListFragment){
            fragmentAtual = ReportListFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_map -> {
                fragmentAtual = fragmentMap
            }
            R.id.nav_item_reports -> {
                fragmentAtual = ReportListFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_faq -> {
                fragmentAtual = FaqFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_notice -> {
                fragmentAtual = NoticeFragment()
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container_frame, fragmentAtual!!)
                ft.commit()
            }
            R.id.nav_item_sair_conta -> {

                if(Firebase.auth.currentUser != null){
                    Firebase.auth.signOut()
                    configurarContaDrawer()
                    Toast.makeText(this, "Saindo da conta", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Você não está logado!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_item_sair -> {
                exibirDialogSaida()
            }
            else -> {
                Toast.makeText(this, "Menu default", Toast.LENGTH_SHORT).show()
            }
        }
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
        drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === REQUEST_REPORT_CREATE && resultCode === RESULT_OK) {
            (fragmentAtual as MapViewFragment).loadHeatMap(false)
        }
        else  if (requestCode === REQUEST_REPORT_EDIT && resultCode === RESULT_OK) {
            (fragmentAtual as ReportListFragment).configurarAdapter()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            exibirDialogSaida()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun exibirDialogSaida(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Você deseja sair do aplicativo?")
            .setNegativeButton("Não", null)
            .setPositiveButton("Sim") { _, _ ->
                super.onBackPressed()
            }
        val alert = builder.create()
        alert.show()
    }
}