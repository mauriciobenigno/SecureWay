package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sw_district")
data class District(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "district")
    val district : String,
    @ColumnInfo(name = "hq")
    val hq : String,
    @ColumnInfo(name = "density")
    val density : Double,
    @ColumnInfo(name = "lat")
    val lat : Double,
    @ColumnInfo(name = "lon")
    val lon : Double
) : Serializable