package com.whereicaneat.ui.registro

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.db.entities.usuario
import com.whereicaneat.domain.data.remote.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repository: Repositorio
):
    ViewModel() {

    var nombre: String? = null
    var telefono: String? = null
    var regListener: RegistroListener? = null
    //Se registra el usuario en local y firebase
    fun guardarUsuario(usuario: usuario){
        CoroutineScope(Dispatchers.Main).launch{
            repository.insertarUsuarioLocal(usuario)
            repository.setUsuarioRemote(usuario)
        }
    }

    fun fetchUsuariosFb(): LiveData<MutableList<usuario>>{
        val mutableData = MutableLiveData<MutableList<usuario>>()
        repository.getUsuariosRemote().observeForever { usuariosList ->
            mutableData.value = usuariosList
        }
        return mutableData
    }

    fun onRegistroBotonClicked(v: View){
        if(validar(nombre!!, telefono!!)){
            val usuario: usuario = usuario(null, nombre!!, telefono!!)
            guardarUsuario(usuario)
            regListener?.onSuccess()
        }

    }

    fun validar(name: String, telefono: String): Boolean{
        var result = true


        if(name.length < 3 || telefono.isEmpty()){
            //registro_nombre.setError("Al menos 3 caracteres")
            result = false
        }
        else if(!android.util.Patterns.PHONE.matcher(telefono).matches()){
            regListener?.onFailed("teléfono inválido")
            result = false
        }
        return result
    }
}