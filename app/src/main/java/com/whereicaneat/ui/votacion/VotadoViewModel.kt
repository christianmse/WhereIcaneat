package com.whereicaneat.ui.votacion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Participacion
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class VotadoViewModel(
    val repository: Repositorio
): ViewModel() {
    val contadorVotos = MutableLiveData<MutableList<Participacion>>()
    fun sendVoto(
        token: String,
        restaurantesList: MutableList<Restaurante>,
        remitente: String
    ) {
        repository.sendVoto(token, restaurantesList, remitente)
    }


    fun votosAfterVote(tokenRemitente: String,
                       restaurantesList: MutableList<Restaurante>
                       ) {
        var aux: MutableList<Participacion>? = mutableListOf<Participacion>()
        CoroutineScope(Dispatchers.Main).launch{
            try {
                repository.getParticipantesRemote(tokenRemitente).observeForever {participaciones ->
                    participaciones.forEach {participacion ->
                        restaurantesList.forEach { restauranteElegido ->
                            if(participacion.restaurante == restauranteElegido.nombre){
                                aux?.add(participacion)
                            }
                        }
                    }
                    contadorVotos.value = aux
                }
            }catch (e: Exception){
                Log.e("VotosAfterVote", e.toString())
            }
        }

    }





}