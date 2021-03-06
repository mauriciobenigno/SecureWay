package com.mauriciobenigno.secureway.service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    //private var BASE_URL = "https://raw.githubusercontent.com/mauriciobenigno/trab_igti/main/"
    private var BASE_URL = "http://api-secureway-igti.herokuapp.com/"

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getEndpoints() : Endpoints {
        return getRetrofit().create(Endpoints::class.java)
    }

}