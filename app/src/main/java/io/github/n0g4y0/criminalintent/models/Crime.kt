package io.github.n0g4y0.criminalintent.models

import java.util.*

/*
* Clase para manejar los crimenes, representa el MODELO del proyecto.
* */

data class Crime(val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false,
                 var requiredPolice: Boolean = false)