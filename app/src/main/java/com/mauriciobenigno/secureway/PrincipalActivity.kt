package com.mauriciobenigno.secureway

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.mauriciobenigno.secureway.ui.MapViewFragment


class PrincipalActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var fragmentMap: MapViewFragment? = null
    private var fragmentAtual: Fragment? = null

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

        navigationView = findViewById(R.id.navView);
        navigationView!!.setNavigationItemSelectedListener(this);

        // Inicializar Framents
        fragmentMap = MapViewFragment()
        fragmentAtual = fragmentMap
    }

    override fun onResume() {
        super.onResume()
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragmentAtual!!)
            ft.commit()
        }
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
            else -> {
                Toast.makeText(this, "Menu Default", Toast.LENGTH_SHORT).show()
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
                    .setPositiveButton("Sim") { dialog, id ->
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