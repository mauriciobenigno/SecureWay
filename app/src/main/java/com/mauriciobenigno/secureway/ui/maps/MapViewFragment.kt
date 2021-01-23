package com.mauriciobenigno.secureway.ui


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.mauriciobenigno.secureway.R
import kotlinx.android.synthetic.main.fragment_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapViewFragment : Fragment() {

    lateinit var viewModel : MapViewModel

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var searchView: SearchView? = null
    private var fabButton: ExtendedFloatingActionButton? = null
    private var marker: Marker? = null
    private var mMarkerAtivo = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_maps, container, false)

        setHasOptionsMenu(true);
        mMapView = rootView.findViewById(R.id.mapView)
        mMapView!!.onCreate(savedInstanceState)



        searchView = rootView.findViewById(R.id.edtPesquisa)

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val locationString = searchView!!.query.toString()
                if (!locationString.equals("")) {
                    try {
                        val address =
                            Geocoder(requireActivity().applicationContext).getFromLocationName(
                                locationString,
                                1
                            )
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    address.get(
                                        0
                                    ).getLatitude(), address.get(0).getLongitude()
                                ), 15.0f
                            )
                        )
                    } catch (e: java.lang.Exception) {
                        e.stackTrace
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }
        })

        mMapView!!.getMapAsync { mMap ->
            googleMap = mMap
            loadHeatMap()

            googleMap!!.setOnCameraIdleListener(object : GoogleMap.OnCameraIdleListener {
                override fun onCameraIdle() {
                    if (mMarkerAtivo) {
                        val target: LatLng = googleMap!!.cameraPosition.target
                        if (marker != null) {
                            marker!!.remove()
                            marker = googleMap!!.addMarker(
                                MarkerOptions().position(target).draggable(
                                    true
                                )
                            )
                        } else {
                            marker = googleMap!!.addMarker(
                                MarkerOptions().position(target).draggable(
                                    true
                                )
                            )
                        }
                    }
                }
            })

            googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(p0: Marker?): Boolean {
                    Toast.makeText(context, "VOCÊ CLICOU NUMA ZONA", Toast.LENGTH_LONG).show()
                    return true
                }
            })

        }

        mMapView!!.onResume()

        fabButton = rootView.findViewById(R.id.fab) as ExtendedFloatingActionButton

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
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        fabButton!!.setOnClickListener {
            doAsync {
                viewModel.refreshData()
            }
        }
        loadHeatMap()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (googleMap != null) {
            outState.putParcelable("STATE_KEY_MAP_CAMERA", googleMap!!.cameraPosition);
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            R.id.action_my_location -> {
                googleMap!!.isMyLocationEnabled = true
            }
            R.id.action_view_marker -> {
                if (mMarkerAtivo) {
                    item.setIcon(R.drawable.ic_visibility_off)
                    removerMarker()
                    mMarkerAtivo = false
                } else {
                    item.setIcon(R.drawable.ic_visible_on)
                    habilitarMarker()
                    mMarkerAtivo = true
                }

            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun habilitarMarker() {
        val target: LatLng = googleMap!!.cameraPosition.target
        marker = googleMap!!.addMarker(MarkerOptions().position(target).draggable(true))
    }

    private fun removerMarker() {
        marker!!.remove()
    }


    fun loadHeatMap(){
        try {
            doAsync {
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                val data = viewModel.getHeatMapData()

                val heatMapProvider = HeatmapTileProvider.Builder()
                    .weightedData(data) // load our weighted data
                    .radius(50) // optional, in pixels, can be anything between 20 and 50
                    .maxIntensity(1000.0) // set the maximum intensity
                    .build()

                uiThread {
                    googleMap!!.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))

                    val indiaLatLng = LatLng(20.5937, 78.9629)
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 5f))
                }
            }
        }
        catch (e: java.lang.Exception){
            Log.e("ErroMAP", e.message!!)
        }
    }
}