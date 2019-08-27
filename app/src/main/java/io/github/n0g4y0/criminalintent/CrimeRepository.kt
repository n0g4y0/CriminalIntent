package io.github.n0g4y0.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.github.n0g4y0.criminalintent.database.CrimeDatabase
import io.github.n0g4y0.criminalintent.models.Crime
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context){

    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
        ).build()

    private val crimeDao = database.crimeDao()
    // creando variable para usar un objeto EXCECUTOR, clase para el manejo de actualizaciones y registros en la BD:
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)
    /*
    * funcion que emplea el objeto EXECUTOR, para actualizar:
    * */
    fun updateCrime(crime:Crime){
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }
    /*
    * funcion que emplea el objeto EXECUTOR, para agregar nuevos datos Crime:
    * */
    fun addCrime(crime:Crime){
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null


        fun initialize(context:Context){
            if(INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get():CrimeRepository{
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be  initialized")
        }
    }

}