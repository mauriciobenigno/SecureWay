package com.mauriciobenigno.secureway

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_item_one -> {
                fragment = MapViewFragment()
                Toast.makeText(this, "Menu 1 - mapa", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_two -> {
                fragment = MapViewFragment()
                Toast.makeText(this, "Menu 2", Toast.LENGTH_SHORT).show()
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
        if (fragment != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame, fragment)
            ft.commit()
        }
        drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }

}