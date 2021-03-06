package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sw_adjetivo")
data class Adjetivo (
    @ColumnInfo(name = "id_adjetivo")
    @PrimaryKey(autoGenerate = false)
    var id_adjetivo: Long,
    @ColumnInfo(name = "negativo")
    var negativo: Int,
    @ColumnInfo(name = "descricao")
    var descricao: String
): Serializable

