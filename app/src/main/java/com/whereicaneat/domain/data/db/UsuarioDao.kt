package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whereicaneat.domain.data.db.entities.usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: usuario)
    @Delete
    suspend fun eliminarUsuario(usuario: usuario)
    @Query("SELECT * FROM usuario")
    fun getUsuarios(): LiveData<usuario>
}