package com.whereicaneat.ui.registro

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.domain.data.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RegistroViewModel(
    private val repository: Repositorio
):
    ViewModel() {
    var titulo = MutableLiveData<String>()
    var uriImagen: String? = null
    var nombre: String? = null
    var telefono: String? = null
    var regListener: RegistroListener? = null



    //Se registra el usuario en local y firebase
    fun guardarUsuario(Usuario: Usuario){
        CoroutineScope(Dispatchers.Main).launch{
            try {
                repository.insertarUsuarioLocal(Usuario)
                repository.setUsuarioRemote(Usuario)
            }catch (e:Exception){
                Log.e("guardarUsuario", e.toString())
            }
        }
    }

    fun usuarioRegistrado():Boolean{
        var resul = false
        if(repository.getUsuarioLocal() != null)
            resul = true
        return resul
    }




    fun onRegistroBotonClicked(v: View){
        if(validar(nombre!!, telefono!!)){
            val Usuario: Usuario = Usuario(uriImagen!!, nombre!!, telefono!!)
            val response = repository.userLogin()
            guardarUsuario(Usuario)
            regListener?.onSuccess(response)
        }

    }

    fun login(){
        repository.userLogin()
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