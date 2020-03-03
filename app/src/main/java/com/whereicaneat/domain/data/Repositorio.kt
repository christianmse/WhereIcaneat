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
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.domain.data.remote.RegistroFirebase
import java.io.File

class Repositorio(
    private val db:DatabaseLocal
): RegistroFirebase {
    val databasefb = FirebaseDatabase.getInstance()
    val storagefb: FirebaseStorage = FirebaseStorage.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()



    suspend fun insertarUsuarioLocal(Usuario: Usuario) =
        db.getUsuarioDao().insertar(Usuario)

    suspend fun eliminarUsuarioLocal(Usuario: Usuario) =
        db.getUsuarioDao().eliminarUsuario(Usuario)

    fun getUsuariosLocal() = db.getUsuarioDao().getUsuarios()

    suspend fun getUsuariosRemote(): LiveData<MutableList<Usuario>>{
        val myRef = databasefb.getReference("Usuarios")
        val usuariosList = MutableLiveData<MutableList<Usuario>>()

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            val listData = mutableListOf<Usuario>()
            override fun onCancelled(p0: DatabaseError) {
                Log.e("Error_ValueListenerAdapte", "on Cancelled$p0")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    for(data in p0.children){
                        val usuario = data.getValue(Usuario::class.java)
                       listData.add(usuario!!)
                    }
                    usuariosList.value = listData
                }
            }

        })
        return usuariosList
    }

    fun currentUserReference(): DatabaseReference {
        val currentUid = databasefb.getReference("Usuarios").child("Usuarios").child(mAuth.currentUser!!.uid)
        return currentUid
    }


    suspend fun insertarRestauranteLocal(restaurante: restaurante) =
        db.getRestauranteDao().insertar(restaurante)

    suspend fun eliminarRestauranteLocal(restaurante: restaurante) =
        db.getRestauranteDao().eliminar(restaurante)

    fun getRestaurantesLocal() =
        db.getRestauranteDao().getTodosLosRestaurantes()

    fun getRestaurantesRemote():LiveData<MutableList<restaurante>>{
        val mutableData = MutableLiveData<MutableList<restaurante>>()
        FirebaseFirestore.getInstance().collection("Restaurantes").get().addOnSuccessListener {result ->
            val listData = mutableListOf<restaurante>()
            for(document in result){
                val imageUrl = document.getString("imageUrl")
                val nombre = document.getString("nombre")
                val website = document.getString("website")
                val aux: restaurante = restaurante(imageUrl!!, nombre!!, website!!)
                listData.add(aux)
            }
            mutableData.value = listData
        }
        return mutableData
    }

    override fun setUsuarioRemote(Usuario: Usuario){
        val myRef = databasefb.getReference("Usuarios")
        val myRefStg = storagefb!!.getReference("Imagenes_Perfil")
        try {
            //database
            myRef.setValue(Usuario)
            //storage
            val uri: Uri = Uri.parse(Usuario.imageUri)
            val file:File = File(uri.path)
            myRefStg.child(Usuario.telefono).putFile(uri)
                .addOnSuccessListener {
                    Log.e("repositorio_Success", it.toString())
                }
        } catch (e: Exception){
            Log.e("repositorio_Catch", e.toString())
        }
    }

    fun userLogin(): LiveData<Boolean>{
        val resul = MutableLiveData<Boolean>()

        mAuth.signInAnonymously().addOnFailureListener {
            resul.value = false
        }
            .addOnSuccessListener {
                resul.value = true
            }

        return resul as LiveData<Boolean>
    }


}