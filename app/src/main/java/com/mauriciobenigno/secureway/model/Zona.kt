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
    @ColumnInfo(name = "cordenada_x")
    var cordenada_x: Double,
    @ColumnInfo(name = "cordenada_y")
    var cordenada_y: Double,
    @ColumnInfo(name = "densidade")
    var densidade: Double
): Serializable

