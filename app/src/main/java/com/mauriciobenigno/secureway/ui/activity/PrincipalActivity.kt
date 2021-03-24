package com.mauriciobenigno.secureway.ui.activity

import android.content.Intent
import android.os.Bundle
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
import com.github.rtoshiro.util.format.MaskFormatter
import com.google.android.gms.maps.MapFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.MapViewFragment
import com.mauriciobenigno.secureway.ui.activity.autenticacao.AutenticacaoActivity
import org.w3c.dom.Text


class PrincipalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
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

           /* if(fragmentAtual is MapViewFragment){
                // Atualizar o frament mapa com o novo ponto
                (fragmentAtual as MapViewFragment).loadHeatMap(false)
            }*/
        }
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

    override fun onResume() {
        super.onResume()
        configurarContaDrawer()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> {
                fragmentAtual = fragmentMap
                Toast.makeText(this, "Menu 1 - mapa", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_two -> {
                fragmentAtual = fragmentMap
                Toast.makeText(this, "Menu 2 - Mesmo mapa", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_three -> {
                Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_four -> {
                Toast.makeText(this, "Menu 4", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_sair -> {
                Firebase.auth.signOut()
                configurarContaDrawer()
                Toast.makeText(this, "Saindo da conta", Toast.LENGTH_SHORT).show()

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


    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}