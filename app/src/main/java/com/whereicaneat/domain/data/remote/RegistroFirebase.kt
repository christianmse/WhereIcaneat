package com.whereicaneat.domain.data.remote

import com.whereicaneat.domain.data.db.entities.Usuario

interface RegistroFirebase {

    fun setUsuarioRemote(Usuario: Usuario)

}