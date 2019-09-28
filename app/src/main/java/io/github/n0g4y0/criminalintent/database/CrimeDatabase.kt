package io.github.n0g4y0.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.n0g4y0.criminalintent.models.Crime

/*
* con la clase TYPECONVERTERS, manejamos la conversion de tipos de datos complejos, ejm: DATE y UUID
* */

@Database(entities = [ Crime::class ],version = 2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase: RoomDatabase() {

    abstract fun crimeDao():CrimeDao
}

val migration_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}