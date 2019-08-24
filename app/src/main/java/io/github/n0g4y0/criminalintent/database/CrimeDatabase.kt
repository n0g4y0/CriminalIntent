package io.github.n0g4y0.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.n0g4y0.criminalintent.models.Crime

/*
* con la clase TYPECONVERTERS, manejamos la conversion de tipos de datos complejos, ejm: DATE y UUID
* */

@Database(entities = [ Crime::class ],version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {

    abstract fun crimeDao():CrimeDao
}