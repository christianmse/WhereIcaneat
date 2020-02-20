package com.whereicaneat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.data.db.entities.restaurante
import com.whereicaneat.data.db.entities.usuario

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
        val myRef = databasefb.getReference("Usuarios")
        val mutableData = MutableLiveData<MutableList<usuario>>()
        val listData = mutableListOf<usuario>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(usuario::class.java)
                listData.add(value!!)


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        mutableData.value = listData
        return mutableData
    }

    fun setUsuarioRemote(usuario: usuario){
        val myRef = databasefb.getReference("Usuarios")
        myRef.setValue(usuario)
    }


}