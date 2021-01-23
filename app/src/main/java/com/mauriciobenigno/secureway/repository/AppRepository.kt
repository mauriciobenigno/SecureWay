package com.mauriciobenigno.secureway.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.database.AppDatabase
import com.mauriciobenigno.secureway.model.District
import com.mauriciobenigno.secureway.service.ApiService
import org.jetbrains.anko.doAsync
import org.json.JSONArray

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppRepository(context: Context) {

    val database = AppDatabase.getInstance(context)

    fun getAllLocais() = database.Dao().getAllLocations()

    fun getLiveAllDistric() = database.Dao().getLiveAllDistrict()

    fun getHeatMapData(): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()
        val districts = database.Dao().getAllDistrict()
        districts.forEach {
            if(it.density != 0.0)
                data.add(WeightedLatLng(LatLng(it.lat, it.lon), it.density))
        }
        return data
    }

    fun saveSaveOnServer(district: District) {
        //val request = ApiService.getEndpoints()
        /*request.saveReport(district).enqueue(
            object : Callback<District> {
                override fun onFailure(call: Call<District>, t: Throwable) {
                    Log.e("Erro", "Erro ao cadastrar")
                }

                override fun onResponse(call: Call<District>, response: Response<District>) {
                    if(response.code() == 201) {
                        response.body()?.let {
                            doAsync {
                               // database.Dao().addSingleProduct(it)
                            }
                        }
                    }
                }
            }
        )*/
        database.Dao().insertDistrict(district)
    }
/*
    fun generateHeatMapData(context: Context): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()

        val jsonData = getJsonDataFromAsset(context,"district_data.json")
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
    }*/

    fun fetchLocationsFromServer() {
        val request = ApiService.getEndpoints()
        request.getAllDistricts().enqueue(object : Callback<List<District>> {
            override fun onFailure(call: Call<List<District>>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")
            }

            override fun onResponse(call: Call<List<District>>, response: Response<List<District>>) {
                if (response.code() == 200) {
                    val resultado = response.body()
                    try {
                        resultado?.let { districts ->
                            doAsync {
                                database.Dao().insertAllDistricts(districts)
                            }
                        }
                    }catch (e: Exception){
                        Log.e("ErroAPI",e.message!!)
                    }

                }
            }
        })
    }

    fun fetchDataFromServer() {
        /*val request = ApiService.getEndpoints()
        request.getAllProduts().enqueue(object : Callback<List<Produto>> {
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.wtf("Falha", "Requisição Falhou!")
            }

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.code() == 200) {
                    val resultado = response.body()
                    resultado?.let { produtos ->
                        doAsync {
                            database.Dao().allAllProducts(produtos)
                        }
                    }
                }
            }
        })*/
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