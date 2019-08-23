package io.github.n0g4y0.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.n0g4y0.criminalintent.models.Crime

@Database(entities = [ Crime::class ],version = 1)
abstract class CrimeDatabase: RoomDatabase() {
}