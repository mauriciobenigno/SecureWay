package com.mauriciobenigno.secureway.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mauriciobenigno.secureway.model.Local

@Dao
interface AppDao {

    @Query("select * from sw_local")
    fun getAllLocations() : List<Local>

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