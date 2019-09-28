package io.github.n0g4y0.criminalintent.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
* Clase para manejar los crimenes, representa el MODELO del proyecto.
* */

@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false,
                 var suspect: String = "")