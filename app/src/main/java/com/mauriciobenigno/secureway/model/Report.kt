package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "sw_report")
data class Report (
    @ColumnInfo(name = "id_report")
    @PrimaryKey(autoGenerate = true)
    var id_report: Long,
    @ColumnInfo(name = "id_zona")
    var id_zona: Long,
    @ColumnInfo(name = "numero")
    var numero: Long,
    @ColumnInfo(name = "data_report")
    var data_report: String,
    @ColumnInfo(name = "densidade")
    var densidade: Double,
    @ColumnInfo(name = "observacao")
    var observacao: String? = null
): Serializable

