package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sw_zona")
data class Zona (
    @ColumnInfo(name = "id_zona")
    @PrimaryKey(autoGenerate = false)
    var id_zona: Long,
    @ColumnInfo(name = "coordenada_x")
    var coordenada_x: Double,
    @ColumnInfo(name = "coordenada_y")
    var coordenada_y: Double,
    @ColumnInfo(name = "densidade")
    var densidade: Double
): Serializable

