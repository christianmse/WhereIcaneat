package com.whereicaneat.ui.registro

import android.content.SharedPreferences
import android.os.AsyncTask
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.whereicaneat.common.Common
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.coroutines.*


class RegistroViewModel(
    private val repository: Repositorio
): ViewModel() {
    var titulo = MutableLiveData<String>()
    var uriImagen: String? = null
    var nombre: String? = null
    var telefono: String? = null
    var regListener: RegistroListener? = null
    val TAG: String = "TOKEN"
    val databasefb = FirebaseDatabase.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val uid = mAuth.currentUser?.uid
    lateinit var token: String
    lateinit var usuarioReg:SharedPreferences



    //Se registra el usuario en local y firebase
    fun guardarUsuario(Usuario: Usuario){
        CoroutineScope(Dispatchers.Main).launch{
            try {
                setSingleton(Usuario)
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
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        usuarioReg = v.context.getSharedPreferences(Common.PREF_NAME, Common.PRIVATE_MODE)

        if(validar(nombre!!, telefono!!)){
            usuarioReg.edit().putBoolean("registrado", true).apply()
            //Get user token and register
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    this.token = task.result?.token!!
                    val uid = mAuth.currentUser?.uid
                    val Usuario: Usuario = Usuario(uriImagen!!, nombre!!, telefono!!, token, uid)
                    Log.d("Usuario registro", Usuario.toString())
                    val response = repository.userLogin()
                    guardarUsuario(Usuario)
                    regListener?.onSuccess(response)
                })

        }

    }

    private fun setSingleton(usuario: Usuario) {
        CurrentUser.nombre = usuario.nombreUsuario!!
        CurrentUser.uid = usuario.uid!!
        CurrentUser.token = usuario.token!!
        CurrentUser.telefono = usuario.telefono
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
