package com.whereicaneat.ui.votacion

import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante

class VotadoViewModel(
    val repository: Repositorio
): ViewModel() {
    fun sendVoto(
        token: String,
        restaurantesList: MutableList<Restaurante>,
        remitente: String
    ) {
        repository.sendVoto(token, restaurantesList, remitente)
    }


}