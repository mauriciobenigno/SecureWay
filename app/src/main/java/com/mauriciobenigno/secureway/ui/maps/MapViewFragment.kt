package com.mauriciobenigno.secureway.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.mauriciobenigno.secureway.R


class MapViewFragment : Fragment() {

    lateinit var viewModel : MapViewModel

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_maps, container, false)
        mMapView = rootView.findViewById(R.id.mapView)
        mMapView!!.onCreate(savedInstanceState)



        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
        //viewModel = ViewModelProviders.of(this).get(MapViewFragment::class.java)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        mMapView!!.getMapAsync { mMap ->
            googleMap = mMap

            // For showing a move to my location button
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@getMapAsync
            }
            googleMap!!.isMyLocationEnabled = true

            val data = viewModel.generateHeatMapData(requireActivity().applicationContext)

            val heatMapProvider = HeatmapTileProvider.Builder()
                .weightedData(data) // load our weighted data
                .radius(50) // optional, in pixels, can be anything between 20 and 50
                .maxIntensity(1000.0) // set the maximum intensity
                .build()

            googleMap!!.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))

            val indiaLatLng = LatLng(20.5937, 78.9629)
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 5f))
        }
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }

}