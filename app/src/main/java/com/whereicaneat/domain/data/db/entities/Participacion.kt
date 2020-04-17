package com.whereicaneat.domain.data.db.entities

data class Participacion(
    val restaurante: String
) {
    val participantes: MutableList<Usuario> = mutableListOf()
        get() = field

    fun setParticipante(usuario: Usuario) =
        participantes.add(usuario)
}