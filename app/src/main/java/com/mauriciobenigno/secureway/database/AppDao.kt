package com.mauriciobenigno.secureway.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mauriciobenigno.secureway.model.*

@Dao
interface AppDao {

    @Query("select * from sw_local")
    fun getAllLocations() : List<Local>

    @Query("select * from sw_district")
    fun getAllDistrict() : List<District>

    @Query("select * from sw_district")
    fun getLiveAllDistrict() : LiveData<List<District>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDistricts(districts: List<District>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDistrict(district: District)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSingleZona(zona: Zona)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllZonas(zonas: List<Zona>)

    @Query("select * from sw_zona")
    fun getAllZonas() : List<Zona>

    @Query("select * from sw_zona where id_zona = :zona_id limit 1")
    fun getZonaById(zona_id: Long) : Zona?

    //@Query("select * from sw_zona where id_zona = :zona_id limit 1")
    @Query("SELECT * FROM sw_zona ORDER BY ((:latitude-sw_zona.coordenada_x)*(:latitude-sw_zona.coordenada_y)) + ((:longitude - sw_zona.coordenada_x)*(:longitude - sw_zona.coordenada_y)) ASC LIMIT 1")
    fun getZonaByLocation(latitude: Double, longitude: Double) : Zona?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAdjetivos(adjetivos: List<Adjetivo>)

    @Query("select * from sw_adjetivo where negativo = 0")
    fun getAllAdjetivosPositivos() : List<Adjetivo>

    @Query("select * from sw_adjetivo where negativo = 1")
    fun getAllAdjetivosNegativos() : List<Adjetivo>

    @Query("select * from sw_adjetivo where negativo = :posicao")
    fun getAllAdjetivosFiltrado(posicao: Boolean) : LiveData<List<Adjetivo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // quando um mais atual chegar, substitui
    fun addSingleReport(report: Report)

    @Query("delete from sw_report where id_report = :id_report")
    fun deleteReport(id_report: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReports(reports: List<Report>)

    @Query("select * from sw_report")
    fun getAllReports() : List<Report>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFaq(reports: List<Faq>)

    @Query("select * from sw_faq")
    fun getAllFaq() : List<Faq>

    /*@Query("select * from mxsprodut")
    fun getAllLiveProducts() : LiveData<List<Produto>>

    @Query("select * from mxsprodut where enviafv = :ativado")
    fun getAllProductsEnviaFV(ativado : Boolean) : List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSingleProduct(novoProduto : Produto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun allAllProducts(novoProduto : List<Produto>)

    @Update
    fun updateProduct(produtoAtualizado : Produto)

    @Query("delete from mxsprodut")
    fun ferrarOBanco()*/

}