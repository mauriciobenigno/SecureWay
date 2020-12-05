package com.mauriciobenigno.secureway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng

import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mMap: GoogleMap

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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val data = generateHeatMapData()

        val heatMapProvider = HeatmapTileProvider.Builder()
            .weightedData(data) // load our weighted data
            .radius(50) // optional, in pixels, can be anything between 20 and 50
            .maxIntensity(1000.0) // set the maximum intensity
            .build()

        googleMap.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))

        val indiaLatLng = LatLng(20.5937, 78.9629)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 5f))
    }

    private fun generateHeatMapData(): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()

        val jsonData = getJsonDataFromAsset("district_data.json")
        jsonData?.let {
            for (i in 0 until it.length()) {
                val entry = it.getJSONObject(i)
                val lat = entry.getDouble("lat")
                val lon = entry.getDouble("lon")
                val density = entry.getDouble("density")

                if (density != 0.0) {
                    val weightedLatLng = WeightedLatLng(LatLng(lat, lon), density)
                    data.add(weightedLatLng)
                }
            }
        }

        return data
    }

    private fun getJsonDataFromAsset(fileName: String): JSONArray? {
        try {
            val jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
            return JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
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