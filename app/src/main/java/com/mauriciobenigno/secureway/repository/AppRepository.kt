package com.mauriciobenigno.secureway.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.database.AppDatabase
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.service.ApiService
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


class AppRepository(context: Context) {

    val database = AppDatabase.getInstance(context)

    fun getAllLocais() = database.Dao().getAllLocations()

    fun getLiveAllDistric() = database.Dao().getLiveAllDistrict()

    fun getAllAdjetivosFiltrado(posicao: Boolean) = database.Dao().getAllAdjetivosFiltrado(posicao)

    fun getAllAdjetivosPositivos() = database.Dao().getAllAdjetivosPositivos()

    fun getAllAdjetivosNegativos() = database.Dao().getAllAdjetivosNegativos()

    fun getAllReports() = database.Dao().getAllReports()

    fun getZonaById(zona_id: Long) = database.Dao().getZonaById(zona_id)

    fun getHeatMapData(): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()
        val zonas = database.Dao().getAllZonas()
        zonas.forEach {
            if(it.densidade != 0.0){
                data.add(WeightedLatLng(LatLng(it.coordenada_x, it.coordenada_y), it.densidade))
            }
        }
        return data
    }

    fun saveReportOnServer(report: Pair<Report, Coordenada>) {
        val request = ApiService.getEndpoints()
        request.saveReportOnServer(report).enqueue(
            object : Callback<Pair<Report?, Zona?>> {
                override fun onFailure(call: Call<Pair<Report?, Zona?>>, t: Throwable) {
                    Log.e("Erro", "Erro ao cadastrar")
                }

                override fun onResponse(call: Call<Pair<Report?, Zona?>>, response: Response<Pair<Report?, Zona?>>) {
                    if (response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                                if(it.first != null)
                                    database.Dao().addSingleReport(it.first!!)

                                if(it.second != null)
                                    database.Dao().addSingleZona(it.second!!)
                            }
                        }
                    }
                }
            }
        )
    }

    fun saveZonaOnServer(zona: Zona) {
        val request = ApiService.getEndpoints()
        request.saveZonaOnServer(zona).enqueue(
            object : Callback<Zona> {
                override fun onFailure(call: Call<Zona>, t: Throwable) {
                    Log.e("Erro", "Erro ao cadastrar")
                }

                override fun onResponse(call: Call<Zona>, response: Response<Zona>) {
                    if (response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                                database.Dao().addSingleZona(it)
                            }
                        }
                    }
                }
            }
        )
    }

    fun fetchLocationsFromServer() {
        val request = ApiService.getEndpoints()
        request.getAllDistricts().enqueue(object : Callback<List<District>> {
            override fun onFailure(call: Call<List<District>>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")
            }

            override fun onResponse(
                call: Call<List<District>>,
                response: Response<List<District>>
            ) {
                if (response.code() == 200) {
                    val resultado = response.body()
                    try {
                        resultado?.let { districts ->
                            doAsync {
                                database.Dao().insertAllDistricts(districts)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ErroAPI", e.message!!)
                    }

                }
            }
        })
    }

    fun fetchAdjetivoFromServer() {
        val request = ApiService.getEndpoints()
        request.getAllAdjetivos().enqueue(object : Callback<List<Adjetivo>> {
            override fun onFailure(call: Call<List<Adjetivo>>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")
            }

            override fun onResponse(
                call: Call<List<Adjetivo>>,
                response: Response<List<Adjetivo>>
            ) {
                if (response.code() == 200) {
                    val resultado = response.body()
                    try {
                        resultado?.let { adjetivos ->
                            doAsync {
                                database.Dao().insertAllAdjetivos(adjetivos)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ErroAPI", e.message!!)
                    }
                }
            }
        })
    }

    /*fun getZonaByLocation(coordenada: Coordenada) : BlockingQueue<Zona>{
        val request = ApiService.getEndpoints()

        val blockingQueue: BlockingQueue<Zona> = ArrayBlockingQueue(1)

        request.getZonaByLocation(coordenada).enqueue(object : Callback<Zona> {
            override fun onFailure(call: Call<Zona>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")

            }

            override fun onResponse(call: Call<Zona>, response: Response<Zona>) {
                if (response.code() == 200) {
                    val resultado = response.body()
                    try {
                        resultado?.let { zona ->
                            doAsync {
                                database.Dao().addSingleZona(zona)
                                blockingQueue.add(zona)
                            }

                        }
                    } catch (e: Exception) {
                        Log.e("ErroAPI", e.message!!)
                    }
                }
            }
        })

        return blockingQueue
    }
*/


    fun fetchZonasByLocationFromServer(context: Context) {
        val location = getLastKnownLocation(context)
        if(location != null){
            val coordenada = Coordenada(location.latitude, location.longitude)
            val request = ApiService.getEndpoints()
            request.getZonasByLocation(coordenada).enqueue(object : Callback<List<Zona>> {
                override fun onFailure(call: Call<List<Zona>>, t: Throwable) {
                    Log.wtf("Falha", "Requisição Falhou!")
                }

                override fun onResponse(call: Call<List<Zona>>, response: Response<List<Zona>>) {
                    if (response.code() == 201) {
                        val resultado = response.body()
                        try {
                            resultado?.let { zonas ->
                                doAsync {
                                    database.Dao().insertAllZonas(zonas)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ErroAPI", e.message!!)
                        }
                    }
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(context: Context): Location? {
        var mLocationManager: LocationManager? = null
        mLocationManager = context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    /*fun saveProductOnServer(product: Produto) {
        val request = ApiService.getEndpoints()
        request.saveProduct(product).enqueue(
            object : Callback<Produto> {
                override fun onFailure(call: Call<Produto>, t: Throwable) {
                    Log.e("Erro", "Erro ao cadastrar")
                }

                override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                    if(response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                                database.Dao().addSingleProduct(it)
                            }
                        }
                    }
                }
            }
        )
    }*/

}