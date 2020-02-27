package com.whereicaneat.domain.data.remote

import androidx.lifecycle.LiveData
import com.whereicaneat.domain.data.db.entities.usuario

interface RegistroFirebase {
    fun getUsuariosRemote(): LiveData<MutableList<usuario>>
    fun setUsuarioRemote(usuario: usuario)

}