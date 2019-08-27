package io.github.n0g4y0.criminalintent.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.n0g4y0.criminalintent.CrimeRepository
import java.util.*

class CrimeDetailViewModel : ViewModel(){

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()

    var crimeLiveData : LiveData<Crime?> =
            Transformations.switchMap(crimeIdLiveData){crimeId ->
                crimeRepository.getCrime(crimeId)

            }


    fun loadCrime(crimeId: UUID){
        crimeIdLiveData.value = crimeId
    }
}