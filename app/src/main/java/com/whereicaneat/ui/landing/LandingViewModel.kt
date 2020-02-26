package com.whereicaneat.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.db.entities.usuario
import com.whereicaneat.domain.data.remote.Repositorio

class LandingViewModel(
    private val repository: Repositorio
) : ViewModel(){

    //Obtiene usuarios de firestore
    fun fetchUserData():LiveData<MutableList<usuario>>{
        val mutableData = MutableLiveData<MutableList<usuario>>()
        repository.getUsuariosRemote().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}