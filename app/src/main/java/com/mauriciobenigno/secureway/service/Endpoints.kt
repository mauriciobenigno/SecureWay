package com.mauriciobenigno.secureway.service

import com.mauriciobenigno.secureway.model.*
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

    @POST("report/newreport")
    fun saveReportOnServer(@Body report: Pair<Report, Coordenada> ) : Call<Report>

    @POST("zonas/regiao")
    fun getZonasByLocation(@Body coordenada: Coordenada) : Call<List<Zona>>

    @POST("zonas/proxima")
    fun getZonaByLocation(@Body coordenada: Coordenada) : Call<Zona>

    /*@POST("produtos")
    fun saveProduct(@Body district : District) :  Call<District>*/

}