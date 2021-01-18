package com.mauriciobenigno.secureway.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import com.mauriciobenigno.secureway.database.AppDatabase
import org.json.JSONArray


class AppRepository(context: Context) {

    val database = AppDatabase.getInstance(context)

    fun getAllLocais() = database.Dao().getAllLocations()

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
    }

    fun getJsonDataFromAsset(context: Context,fileName: String): JSONArray? {
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            return JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
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