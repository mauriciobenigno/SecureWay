package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sw_local")
data class Local(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_local")
    val idLocal : Long,
    @ColumnInfo(name = "coordenada_x")
    val cordenadaX : Float,
    @ColumnInfo(name = "coordenada_y")
    val cordenadaY : Float,
    @ColumnInfo(name = "opinada")
    val status : Boolean
)