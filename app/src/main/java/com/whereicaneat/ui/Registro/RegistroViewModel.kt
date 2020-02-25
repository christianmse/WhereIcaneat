package com.whereicaneat.ui.Registro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.db.entities.usuario
import com.whereicaneat.domain.data.remote.Repositorio
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repository: Repositorio
):
    ViewModel() {

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

    /*fun validar(): Boolean{
        var result = true
        var name = registro_nombre.text
        var telefono = registro_movil.text

        if(name.length < 3 || telefono.isEmpty()){
            registro_nombre.setError("Al menos 3 caracteres")
            result = false
        }
        else if(!android.util.Patterns.PHONE.matcher(telefono).matches()){
            registro_movil.setError("Introduce un número de télefono válido")
            result = false
        }
        return result
    }*/
}