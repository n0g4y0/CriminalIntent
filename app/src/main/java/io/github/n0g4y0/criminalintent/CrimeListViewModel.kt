package io.github.n0g4y0.criminalintent

import androidx.lifecycle.ViewModel
import io.github.n0g4y0.criminalintent.models.Crime

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    // por ahora, la inicializamos con datos ficticios:
    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crimes += crime
        }
    }
}