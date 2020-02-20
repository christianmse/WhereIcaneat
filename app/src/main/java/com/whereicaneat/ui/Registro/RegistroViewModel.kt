package com.whereicaneat.ui.Registro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.data.db.entities.usuario
import com.whereicaneat.data.repository.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repository: Repositorio):
    ViewModel() {

    fun insertarUsuario(usuario: usuario){
        CoroutineScope(Dispatchers.Main).launch{
            repository.insertarUsuarioLocal(usuario)
        }
        repository.setUsuarioRemote(usuario)
    }

    fun fetchUsuariosFb(): LiveData<MutableList<usuario>>{
        val mutableData = MutableLiveData<MutableList<usuario>>()
        repository.getUsuariosRemote().observeForever { usuariosList ->
            mutableData.value = usuariosList
        }
        return mutableData
    }
}