package com.mauriciobenigno.secureway.service

import com.mauriciobenigno.secureway.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    fun saveReportOnServer(@Body report: Pair<Report, Coordenada> ) : Call<Pair<Report?, Zona?>>

    @POST("report/update")
    fun updateReportOnServer(@Body report: Report ) : Call<Pair<Report?, Zona?>>

    @POST("report/delete")
    fun deleteReportOnServer(@Body report: Report ) : Call<Boolean>

    @GET("zonas/all")
    fun getAllZonas() : Call<List<Zona>>

    @POST("zonas/regiao")
    fun getZonasByLocation(@Body coordenada: Coordenada) : Call<List<Zona>>

    @POST("zonas/proxima")
    fun getZonaByLocation(@Body coordenada: Coordenada) : Call<Zona>

    @POST("report/user")
    fun getReportsByUser(@Body numero: String) : Call<List<Report>>

    @GET("faq/all")
    fun getAllFaq() : Call<List<Faq>>


    /*@POST("produtos")
    fun saveProduct(@Body district : District) :  Call<District>*/

}