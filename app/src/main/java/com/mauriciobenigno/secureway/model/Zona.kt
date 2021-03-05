package com.mauriciobenigno.secureway.model

import java.io.Serializable

data class Zona (
    var zonaId: Long,
    var cordenadaX: Double,
    var cordenadaY: Double,
    var densidade: Double
): Serializable

