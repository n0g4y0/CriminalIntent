package io.github.n0g4y0.criminalintent

import androidx.lifecycle.ViewModel
import io.github.n0g4y0.criminalintent.models.Crime

class CrimeListViewModel : ViewModel() {

    /*
    *
    * las siguientes lineas, extraen los datos, desde unos archivos que acabamos de copiar,
    * que son bases de datos pre cargados.
    *
    * */

     private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime: Crime){
        crimeRepository.addCrime(crime)
    }

}