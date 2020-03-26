package com.whereicaneat.ui.registro

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.whereicaneat.R
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
    lateinit var token: String
    val TAG: String = "TOKEN"



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
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    this.token = task.result?.token!!
                    val Usuario: Usuario = Usuario(uriImagen!!, nombre!!, telefono!!, token)
                    Log.d("Usuario registro", Usuario.toString())
                    val response = repository.userLogin()
                    guardarUsuario(Usuario)
                    regListener?.onSuccess(response)
                })

        }

    }
    fun getToken() {

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                this.token = task.result?.token!!


            })
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