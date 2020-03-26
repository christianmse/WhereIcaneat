package com.whereicaneat.data.db.entities

import androidx.lifecycle.LiveData
import androidx.room.*
import com.whereicaneat.domain.data.db.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(Usuario: Usuario)
    @Delete
    suspend fun eliminarUsuario(Usuario: Usuario)
    @Query("SELECT * FROM usuario")
    fun getUsuario(): LiveData<Usuario>

}