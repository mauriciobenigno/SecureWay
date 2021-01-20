package com.mauriciobenigno.secureway.service

import com.mauriciobenigno.secureway.model.District
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Endpoints {

    @GET("district_data.json")
    fun getAllDistricts() : Call<List<District>>

    /*@POST("produtos")
    fun saveProduct(@Body district : District) :  Call<District>*/

}