package com.mauriciobenigno.secureway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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
        when (item.itemId) {
            R.id.nav_item_one -> {
                Toast.makeText(this, "Menu 1", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_item_two -> {
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
        drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }

}