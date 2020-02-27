package com.whereicaneat.domain.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.db.entities.restaurante
import com.whereicaneat.domain.data.db.entities.usuario
import com.whereicaneat.domain.data.remote.RegistroFirebase
import java.io.File

class Repositorio(
    private val db:DatabaseLocal
): RegistroFirebase {
    val databasefb = FirebaseDatabase.getInstance()
    val storagefb: FirebaseStorage = FirebaseStorage.getInstance()



    suspend fun insertarUsuarioLocal(usuario: usuario) =
        db.getUsuarioDao().insertar(usuario)

    suspend fun eliminarUsuarioLocal(usuario: usuario) =
        db.getUsuarioDao().eliminarUsuario(usuario)

    fun getUsuariosLocal() = db.getUsuarioDao().getUsuarios()

    suspend fun insertarRestauranteLocal(restaurante: restaurante) =
        db.getRestauranteDao().insertar(restaurante)

    suspend fun eliminarRestauranteLocal(restaurante: restaurante) =
        db.getRestauranteDao().eliminar(restaurante)

    fun getRestaurantesLocal() =
        db.getRestauranteDao().getTodosLosRestaurantes()

    override fun getUsuariosRemote():LiveData<MutableList<usuario>>{
        val mutableData = MutableLiveData<MutableList<usuario>>()
        FirebaseFirestore.getInstance().collection("Usuarios").get().addOnSuccessListener {result ->
            val listData = mutableListOf<usuario>()
            for(document in result){
                val imageUrl = document.getString("imageUrl")
                val nombre = document.getString("nombreUsuario")
                val telefono = document.getString("telefono")
                listData.add(usuario(imageUrl!!, nombre!!, telefono!!))
            }
            mutableData.value = listData
        }
        return mutableData
    }

    override fun setUsuarioRemote(usuario: usuario){
        val myRef = databasefb.getReference("Usuarios")
        val myRefStg = storagefb!!.getReference("Imagenes_Perfil")
        try {
            //database
            myRef.child(usuario.telefono).setValue(usuario.nombreUsuario)
            //storage
            val uri: Uri = Uri.parse(usuario.imageUri)
            val file:File = File(uri.path)
            myRefStg.child(usuario.telefono).putFile(uri)
                .addOnSuccessListener {
                    Log.e("repositorio_Success", it.toString())
                }
        } catch (e: Exception){
            Log.e("repositorio_Catch", e.toString())
        }
    }

    fun userLogin(): LiveData<Boolean>{
        val resul = MutableLiveData<Boolean>()
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInAnonymously().addOnFailureListener {
            resul.value = false
        }
            .addOnSuccessListener {
                resul.value = true
            }

        return resul as LiveData<Boolean>
    }


}