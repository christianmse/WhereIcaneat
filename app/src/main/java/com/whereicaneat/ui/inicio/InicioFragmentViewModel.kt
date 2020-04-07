package com.whereicaneat.ui.inicio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class InicioFragmentViewModel(
    private val repository: Repositorio
) : ViewModel() {
    val invitados = MutableLiveData<MutableList<Usuario>>()


     fun getUsuariosRemote(): LiveData<MutableList<Usuario>>{
         CoroutineScope(Dispatchers.Main).launch{
             try {
                 repository.getUsuariosRemote().observeForever {
                     invitados.value = it
                 }
             }catch (e: Exception){
                 Log.e("getUsuariosRemoteVM", e.toString())
             }
         }
         return invitados
    }

    fun sendUsuariosSelected(usuariosSelec: List<Usuario>) {
        repository.sendUsuariosSelected(usuariosSelec)
    }
}




