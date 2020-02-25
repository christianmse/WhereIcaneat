package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whereicaneat.domain.data.db.entities.restaurante

@Dao
interface RestauranteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(restaurante: restaurante)
    @Delete
    suspend fun eliminar(restaurante: restaurante)
    @Query ("SELECT * FROM restaurante")
    fun getTodosLosRestaurantes(): LiveData<List<restaurante>>
}