package com.whereicaneat.ui.resultado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante

class ResultadoViewModel(
    val repositorio: Repositorio
): ViewModel() {

    val mutableData = MutableLiveData<MutableList<Restaurante>>()

    //Obtiene restaurantes de firestore
    fun getRestaurantesData(
        restaurantes: Array<String>?
    ): LiveData<MutableList<Restaurante>> {
        val listData = mutableListOf<Restaurante>()
        repositorio.getRestaurantesRemote().observeForever {
            it.forEach { rest ->
                restaurantes?.forEach { ganador ->
                    if(rest.nombre.equals(ganador)){
                        listData.add(rest)
                    }
                }
            }
            mutableData.value = listData
        }
        return mutableData
    }

}