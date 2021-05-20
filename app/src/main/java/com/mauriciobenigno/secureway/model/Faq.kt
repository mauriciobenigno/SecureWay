package com.mauriciobenigno.secureway.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "sw_faq")
data class Faq (
    @ColumnInfo(name = "id_faq")
    @PrimaryKey(autoGenerate = false)
    var id_zona: Long,
    @ColumnInfo(name = "titulo")
    var titulo: String,
    @ColumnInfo(name = "descricao")
    var descricao: String,
    @ColumnInfo(name = "possui_video")
    var possui_video: Int,
    @ColumnInfo(name = "link")
    var link: String
): Serializable

