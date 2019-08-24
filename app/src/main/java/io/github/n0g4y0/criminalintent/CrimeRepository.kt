package io.github.n0g4y0.criminalintent

import android.content.Context
import androidx.room.Room
import io.github.n0g4y0.criminalintent.database.CrimeDao
import io.github.n0g4y0.criminalintent.database.CrimeDatabase
import java.lang.IllegalStateException

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context){

    private val database : CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
        ).build()

    private val CrimeDao = database.crimeDao()

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