package io.github.n0g4y0.criminalintent.database

import androidx.room.Dao
import androidx.room.Query
import io.github.n0g4y0.criminalintent.models.Crime
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime ")
    fun getCrimes(): List<Crime>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): Crime?
}