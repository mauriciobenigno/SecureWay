package com.mauriciobenigno.secureway.model

import java.io.Serializable
import java.util.*

data class Report (
    var reportId: Long,
    var zonaId: Double,
    var numeroReporter: Double,
    var dataReport: Date,
    var densidadeOpiniao: Double,
    var observacao: String
): Serializable

