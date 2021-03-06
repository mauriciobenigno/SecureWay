package com.mauriciobenigno.secureway.service

import com.mauriciobenigno.secureway.model.Adjetivo
import com.mauriciobenigno.secureway.model.Coordenada
import com.mauriciobenigno.secureway.model.District
import com.mauriciobenigno.secureway.model.Zona
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Endpoints {

    @GET("district_data.json")
    fun getAllDistricts() : Call<List<District>>

    @GET("adjetivos/all")
    fun getAllAdjetivos() : Call<List<Adjetivo>>

    @POST("zonas/newpost")
    fun saveZonaOnServer(@Body zona: Zona) : Call<Zona>

    @POST("zonas/regiao")
    fun getZonasByLocation(@Body coordenada: Coordenada) : Call<List<Zona>>

    /*@POST("produtos")
    fun saveProduct(@Body district : District) :  Call<District>*/

}