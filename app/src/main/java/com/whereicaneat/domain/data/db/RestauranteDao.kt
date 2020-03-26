package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whereicaneat.domain.data.db.entities.Restaurante

@Dao
interface RestauranteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(restaurante: Restaurante)
    @Delete
    suspend fun eliminar(restaurante: Restaurante)
    @Query ("SELECT * FROM restaurante")
    fun getTodosLosRestaurantes(): LiveData<List<Restaurante>>

}