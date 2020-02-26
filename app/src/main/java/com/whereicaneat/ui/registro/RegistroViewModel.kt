package com.whereicaneat.ui.registro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.usuario
import com.whereicaneat.domain.data.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistroViewModel(
    private val repository: Repositorio
):
    ViewModel() {

    lateinit var titulo: MutableLiveData<String>
    var uriImagen: Uri? = null
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

   /* *//*fun dondeEstaElUsuario():LiveData<String>{
        val mutableData = MutableLiveData<String>()
        when (R.layout.activity_registro){
            R.id.registro_movil ->
                R.id.registro_nombre ->
        }*//*


        return mutableData
    }*/
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