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

    fun deleteReport(id_report: Long) = database.Dao().deleteReport(id_report)

    fun getZonaById(zona_id: Long) = database.Dao().getZonaById(zona_id)

    //fun getZonasByLocation(Coo: Long) = database.Dao().getZonaById(zona_id)

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

    fun asyncSaveReportOnServer(report: Pair<Report, Coordenada>) {
        val request = ApiService.getEndpoints()
        request.saveReportOnServer(report).enqueue(
            object : Callback<Pair<Report?, Zona?>> {
                override fun onFailure(call: Call<Pair<Report?, Zona?>>, t: Throwable) {
                    Log.e("Erro", "Erro ao cadastrar")
                }

                override fun onResponse(
                    call: Call<Pair<Report?, Zona?>>,
                    response: Response<Pair<Report?, Zona?>>
                ) {
                    if (response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                                if (it.first != null)
                                    database.Dao().addSingleReport(it.first!!)

                                if (it.second != null)
                                    database.Dao().addSingleZona(it.second!!)
                            }
                        }
                    }
                }
            }
        )
    }

    fun saveReportOnServer(report: Pair<Report, Coordenada>) {
        val request = ApiService.getEndpoints()
        val call = request.saveReportOnServer(report)

        val response =  call.execute()

        if(response.isSuccessful){
            response.body()?.let {
                doAsync {
                    if(it.first != null)
                        database.Dao().addSingleReport(it.first!!)

                    if(it.second != null)
                        database.Dao().addSingleZona(it.second!!)
                }
            }
        }else {
            throw Exception("Ocorreu um erro ao enviar seu report!")
        }
    }

    fun asyncUpdateReportOnServer(report: Report){
        val request = ApiService.getEndpoints()
        request.updateReportOnServer(report).enqueue(
            object : Callback<Pair<Report?, Zona?>> {
                override fun onFailure(call: Call<Pair<Report?, Zona?>>, t: Throwable) {
                    Log.e("Erro", "Erro ao atualizar report")
                }

                override fun onResponse(
                    call: Call<Pair<Report?, Zona?>>,
                    response: Response<Pair<Report?, Zona?>>
                ) {
                    if (response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                                if (it.first != null)
                                    database.Dao().addSingleReport(it.first!!)

                                if (it.second != null)
                                    database.Dao().addSingleZona(it.second!!)
                            }
                        }
                    }
                }
            }
        )
    }

    // Método sincrono
    fun updateReportOnServer(report: Report){
        val request = ApiService.getEndpoints()
        val call = request.updateReportOnServer(report)
        val response =  call.execute()

        if(response.isSuccessful){
            response.body()?.let {

                if(it.first != null)
                    database.Dao().addSingleReport(it.first!!)

                if(it.second != null)
                    database.Dao().addSingleZona(it.second!!)
                return
            }
        }else {
            throw Exception("Ocorreu um erro ao enviar seu report!")
        }
    }

    // Método sincrono
    fun deleteReportOnServer(report: Report): Boolean{
        val request = ApiService.getEndpoints()
        val call = request.deleteReportOnServer(report)
        val response =  call.execute()

        if(response.isSuccessful){
            response.body()?.let {
                if(it){
                    this.deleteReport(report.id_report)
                    return true
                } else {
                    throw Exception("Ocorreu um erro tentar excluir seu report na nuvem!")
                }
            }
        }else {
            throw Exception("Ocorreu um erro ao enviar seu report!")
        }
        return false
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

    fun fetchZonasFromServer() {
        val request = ApiService.getEndpoints()
        request.getAllZonas().enqueue(object : Callback<List<Zona>> {
            override fun onFailure(call: Call<List<Zona>>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")
            }

            override fun onResponse(
                call: Call<List<Zona>>,
                response: Response<List<Zona>>
            ) {
                if (response.code() == 200) {
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

    fun asyncFetchZonasFromServer() {
        val request = ApiService.getEndpoints()
        val call = request.getAllZonas()
        val response =  call.execute()
        if(response.isSuccessful){
            val resultado = response.body()
            try {
                resultado?.let { zonas ->
                    doAsync {
                        database.Dao().insertAllZonas(zonas)
                    }
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", e.message!!)
                throw Exception("Report excluído, porém ocorreu um erro: ${e.message} ")
            }
        } else{
            throw Exception("Report excluído, porém houve um erro ao atualizar zonas!")
        }
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


    fun getZonaByLocation(coordenada: Coordenada) : Zona? {

        val request = ApiService.getEndpoints()
        val call = request.getZonaByLocation(coordenada)
        val response =  call.execute()

        if(response.isSuccessful){
            response.body()?.let {
                database.Dao().addSingleZona(it)
                return it
            }
        }else {
            /*val zona = database.Dao().getZonaByLocation(coordenada.latitude, coordenada.longitude)
            val results = FloatArray(1)
            if(zona != null) {
                Location.distanceBetween(
                    coordenada.latitude,
                    coordenada.longitude,
                    zona.coordenada_x,
                    zona.coordenada_y,
                    results
                )
                return zona
            }*/
        }
        return null
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

}