package com.whereicaneat.data.db.entities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.whereicaneat.domain.data.db.entities.restaurante
import com.whereicaneat.domain.data.db.entities.usuario

@Database(
    entities = [restaurante::class, usuario::class],
    version = 1
)
abstract class DatabaseLocal : RoomDatabase(){

    abstract fun getRestauranteDao(): RestauranteDao
    abstract fun getUsuarioDao(): UsuarioDao



    companion object{
        @Volatile
        private var instance: DatabaseLocal? = null
        private val LOCK = Any()

        private fun crearBd(context: Context) =
            Room.
                databaseBuilder(context.applicationContext,
                DatabaseLocal::class.java,
                    "Database.db").build()

        operator fun invoke(context: Context) = instance?:
        synchronized(LOCK){
            instance?:
            crearBd(context).also { instance = it }}
    }


}