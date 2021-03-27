package com.mauriciobenigno.secureway.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable
import java.util.*

data class ReportZona(
    var report: Report,
    var zona: Zona
): Serializable


