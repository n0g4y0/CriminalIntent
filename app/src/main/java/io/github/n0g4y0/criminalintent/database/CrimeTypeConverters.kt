package io.github.n0g4y0.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

/*
* clase que maneja las conversiones de datos,
* ya que, por defecto, la LIBRARY 'ROOM' solo maneja tipos de datos PRIMITIVOS correctamente,
* y tiene problemas con la manipulacion de tipos de datos  COMPLEJOS (como DATE o UUID)
* */
class CrimeTypeConverters {

    @TypeConverter
    fun fromDate(date:Date?): Long?{
        return date?.time
    }
    /*
    *
    * */

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date?{
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID?{
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String?{
        return uuid?.toString()
    }

}