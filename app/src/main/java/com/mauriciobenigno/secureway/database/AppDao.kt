package com.mauriciobenigno.secureway.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mauriciobenigno.secureway.model.Adjetivo
import com.mauriciobenigno.secureway.model.District
import com.mauriciobenigno.secureway.model.Local

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

    @Query("select * from sw_adjetivo where negativo = 0")
    fun getAllAdjetivosPositivos() : List<Adjetivo>

    @Query("select * from sw_adjetivo where negativo = 1")
    fun getAllAdjetivosNegativos() : List<Adjetivo>

    @Query("select * from sw_adjetivo where negativo = :posicao")
    fun getAllAdjetivosFiltrado(posicao: Boolean) : LiveData<List<Adjetivo>>

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