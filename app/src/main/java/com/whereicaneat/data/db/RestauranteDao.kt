package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RestauranteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(restaurante: restaurante)
    @Delete
    suspend fun eliminar(restaurante: restaurante)
    @Query ("SELECT * FROM restaurante")
    fun getTodosLosRestaurantes(): LiveData<List<restaurante>>
}