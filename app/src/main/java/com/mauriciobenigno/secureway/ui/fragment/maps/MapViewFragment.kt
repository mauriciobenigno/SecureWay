package com.mauriciobenigno.secureway.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.Zona
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity.Companion.REQUEST_REPORT_CREATE
import com.mauriciobenigno.secureway.ui.activity.autenticacao.AutenticacaoActivity
import com.mauriciobenigno.secureway.ui.activity.report.ReportActivity
import com.mauriciobenigno.secureway.ui.adapter.InfoRegiaoAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class MapViewFragment : Fragment() {

    lateinit var viewModel : MapViewModel

    var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var mLocationManager: LocationManager? = null
    private var mLocation: Location? = null
    private var mAddress: Address? = null
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
                        mAddress = Geocoder(requireActivity().applicationContext).getFromLocationName(locationString, 1)[0]
                        googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mAddress!!.latitude, mAddress!!.longitude), 15.0f))
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
                        try{
                            // Atualizando lugar
                            mAddress = Geocoder(requireActivity().applicationContext).getFromLocation(target.latitude,target.longitude, 1)[0]
                        }
                        catch (e: Exception){
                            // Não está num endereço válido
                            e?.message?.let { Log.e("Erro ao mostrar info", it) }
                        }

                        googleMap!!.setInfoWindowAdapter(InfoRegiaoAdapter(context))

                        // Atualizando marker
                        if (marker != null)
                            marker!!.remove()

                        marker = googleMap!!.addMarker(MarkerOptions().position(target).draggable(true))

                        doAsync {
                            // Carregar a zona a partir da coordenada

                            if (viewModel != null){
                                val zona = viewModel.getZonaByLocation(Coordenada(target.latitude, target.longitude)) as Zona?

                                uiThread {
                                    try{
                                        if(zona != null ){
                                            marker!!.tag = zona
                                            marker!!.showInfoWindow();
                                        }
                                    }
                                    catch (e: Exception){
                                        e?.message?.let { it1 ->
                                            Log.e("Erro ao mostrar info",
                                                it1
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            })

            /*googleMap!!.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(p0: Marker?): Boolean {
                    Toast.makeText(context, "VOCÊ CLICOU NUMA ZONA", Toast.LENGTH_LONG).show()
                    return true
                }
            })*/

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

            if(FirebaseAuth.getInstance().currentUser == null){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Você precisa logar para reportar!")
                    .setMessage("Deseja entrar?")
                    .setNegativeButton("Cancelar") { _, _ ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton("Entrar") { _, _ ->
                        val intent = Intent(requireContext(), AutenticacaoActivity::class.java)
                        startActivity(intent)
                    }
                    .show()
                return@setOnClickListener
            }

            try {
                var descricao = ""
                if(mAddress != null){
                    if (mAddress!!.getThoroughfare() != null) {
                        descricao = descricao + "<b>" + "Endereço: " + "</b>" + mAddress!!.getThoroughfare() + "<br>";
                    } else {
                        mAddress!!.setThoroughfare("");
                    }

                    if (mAddress!!.getSubThoroughfare() != null) {
                        descricao = descricao + "<b>" + "Numero: " + "</b>" + mAddress!!.getSubThoroughfare() + "<br>";
                    } else {
                        mAddress!!.setSubThoroughfare("");
                    }

                    if (mAddress!!.getSubLocality() != null) {
                        descricao = descricao + "<b>" + "Bairro: " + "</b>" + mAddress!!.getSubLocality() + "<br>";
                    } else {
                        mAddress!!.setSubLocality("");
                    }

                    if (mAddress!!.getLocality() != null) {
                        descricao = descricao + "<b>" + "Município: " + "</b>" + mAddress!!.getLocality() + "<br>";
                    } else {
                        mAddress!!.setLocality("");
                    }

                    if (mAddress!!.getAdminArea() != null && mAddress!!.getAdminArea().length >= 2) {
                        descricao = descricao + "<b>" + "Estado: " + "</b>" + mAddress!!.getAdminArea() + "<br>";
                    } else {
                        mAddress!!.setAdminArea("");
                    }
                    if (mAddress!!.getPostalCode() != null && mAddress!!.getPostalCode().isNotEmpty()) {
                        descricao = descricao + "<b>" + "CEP: " + "</b>" + mAddress!!.getPostalCode() + "<br>";
                    } else {
                        mAddress!!.setPostalCode("");
                    }
                }

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Reportar o seguinte endereço? ")
                    .setMessage(Html.fromHtml(descricao))
                    .setNegativeButton("Não", null)
                    .setPositiveButton("Sim") { _, _ ->
                        val intent = Intent(requireContext(), ReportActivity::class.java)
                        intent.putExtra("endereco", mAddress)
                        requireActivity().startActivityForResult(intent, REQUEST_REPORT_CREATE)

                        doAsync {
                            /*viewModel.saveZonaOnServer(Zona(0,mAddress!!.latitude,mAddress!!.longitude,500.0))
                            uiThread {
                                loadHeatMap(false)
                            }*/
                        }
                    }
                val alert = builder.create()
                alert.show()
            }
            catch (e : Exception){
                e.message?.let { it1 -> Log.e("ErroGeocoder", it1) }
            }

        }
        loadHeatMap(false)
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
                val location = getLastKnownLocation()
                if (location != null) {
                    mLocation = getLastKnownLocation()
                    mAddress = Geocoder(requireActivity().applicationContext).getFromLocation(mLocation!!.latitude, mLocation!!.longitude,1)[0]
                    googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15.0f))
                }

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

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        mLocationManager =
            requireActivity().applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = mLocationManager!!.getLastKnownLocation(provider) //?: continue
            if (l != null) {
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    bestLocation = l
                }
            }
        }
        return bestLocation
    }


    fun loadHeatMap(moveToIndia: Boolean = true){
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

                val colors = intArrayOf(
                    Color.rgb(0, 0, 0),
                    Color.rgb(95, 51, 132)
                )
                val startPoints = floatArrayOf(0.6f, 1f)
                val gradient = Gradient(colors, startPoints)

                val heatMapProvider = HeatmapTileProvider.Builder()
                    .weightedData(data) // load our weighted data
                    .radius(50) // optional, in pixels, can be anything between 20 and 50
                    .opacity(0.9)
                    //.gradient(gradient)
                    .maxIntensity(1000.0) // set the maximum intensity
                    .build()
                uiThread {
                    googleMap!!.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))
                    if(moveToIndia){
                        val indiaLatLng = LatLng(20.5937, 78.9629)
                        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 5f))
                    }

                }
            }
        }
        catch (e: java.lang.Exception){
            Log.e("ErroMAP", e.message!!)
        }
    }
}