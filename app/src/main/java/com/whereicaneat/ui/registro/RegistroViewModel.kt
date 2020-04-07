package com.whereicaneat.ui.registro

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
    lateinit var token: String
    val TAG: String = "TOKEN"

    val databasefb = FirebaseDatabase.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val uid = mAuth.currentUser?.uid
    val myRef = databasefb.getReference("Usuarios")



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
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        CurrentUser.nombre = nombre!!

        if(validar(nombre!!, telefono!!)){
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
                    //setSingleton(nombre!!, token, uid, telefono!!)
                    Log.d("Usuario registro", Usuario.toString())
                    val response = repository.userLogin()
                    guardarUsuario(Usuario)
                    regListener?.onSuccess(response)
                })

        }

    }

    private fun setSingleton(nombre: String, token: String, uid: String?, telefono: String) {
        CurrentUser.nombre = nombre
        CurrentUser.uid = token
        CurrentUser.token = token
        CurrentUser.telefono = telefono
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

    fun setCurrentUser() {
        repository.setcurrentUser()
    }


}
