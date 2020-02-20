package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: usuario)
    @Delete
    suspend fun eliminarUsuario(usuario: usuario)
    @Query("SELECT * FROM usuario")
    fun getUsuarios(): LiveData<usuario>
}