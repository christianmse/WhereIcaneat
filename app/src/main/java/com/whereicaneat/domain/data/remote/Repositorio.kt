package com.whereicaneat.domain.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.value.IntegerValue
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.db.entities.restaurante
import com.whereicaneat.domain.data.db.entities.usuario

class Repositorio(
    private val db:DatabaseLocal
) {
    val databasefb = FirebaseDatabase.getInstance()


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

    fun getUsuariosRemote():LiveData<MutableList<usuario>>{
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

    fun setUsuarioRemote(usuario: usuario){
        val myRef = databasefb.getReference("Usuarios")
        myRef.setValue(usuario)
    }


}