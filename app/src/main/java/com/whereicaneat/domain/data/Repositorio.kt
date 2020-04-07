package com.whereicaneat.domain.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.domain.data.remote.RegistroFirebase
import com.whereicaneat.ui.registro.RegistroActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class Repositorio(
    private val db:DatabaseLocal
): RegistroFirebase {
    val databasefb = FirebaseDatabase.getInstance()
    val storagefb: FirebaseStorage = FirebaseStorage.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val myRef = databasefb.getReference("Usuarios")
    val myRefStg = storagefb!!.getReference("Imagenes_Perfil")
    val uid = mAuth.currentUser?.uid



    suspend fun insertarUsuarioLocal(Usuario: Usuario) {

        db.getUsuarioDao().insertar(Usuario)
    }


    suspend fun eliminarUsuarioLocal(Usuario: Usuario) =
        db.getUsuarioDao().eliminarUsuario(Usuario)

    fun getUsuarioLocal() = db.getUsuarioDao().getUsuario()

    suspend fun getUsuariosRemote(): LiveData<MutableList<Usuario>>{
        val rootRef = databasefb.reference
        val myRefStg = storagefb!!.getReference("Imagenes_Perfil")
        val usuariosRef = rootRef.child("Usuarios")
        val usuariosList = MutableLiveData<MutableList<Usuario>>()

        usuariosRef.addListenerForSingleValueEvent(object: ValueEventListener {
            val listData = mutableListOf<Usuario>()
            override fun onCancelled(p0: DatabaseError) {
                Log.e("Error_ValueListenerAdapter", "on Cancelled$p0")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    for(data in p0.children){
                            val usuario:Usuario? = data.getValue(Usuario::class.java)
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


    suspend fun insertarRestauranteLocal(restaurante: Restaurante) =
        db.getRestauranteDao().insertar(restaurante)

    suspend fun eliminarRestauranteLocal(restaurante: Restaurante) =
        db.getRestauranteDao().eliminar(restaurante)

    fun getRestaurantesLocal() =
        db.getRestauranteDao().getTodosLosRestaurantes()

    fun getRestaurantesRemote():LiveData<MutableList<Restaurante>>{
        val mutableData = MutableLiveData<MutableList<Restaurante>>()
        FirebaseFirestore.getInstance().collection("Restaurantes").get().addOnSuccessListener {result ->
            val listData = mutableListOf<Restaurante>()
            for(document in result){
                val imageUrl = document.getString("imageUrl")
                val nombre = document.getString("nombre")
                val website = document.getString("website")
                if(imageUrl != null && nombre != null && website != null)
                {
                    var aux: Restaurante = Restaurante(imageUrl!!, nombre!!, website!!)
                    listData.add(aux)
                }

            }
            mutableData.value = listData
        }
        return mutableData
    }

    override fun setUsuarioRemote(Usuario: Usuario){


        try {
            //database
            if (uid != null) {
                myRef.child(uid).setValue(Usuario)
                //storage
                val uri: Uri = Uri.parse(Usuario.imageUri)
                val file:File = File(uri.path)
                myRefStg.child(uid).putFile(uri)
                    .addOnSuccessListener {
                        Log.e("repositorio_Success", it.toString())
                    }
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

    fun sendUsuariosSelected(usuariosSelec: List<Usuario>) {
        var notificationsRef = databasefb.getReference("Notifications").push()
        var receivers= HashMap<String,String>()
        var valores:String = ""

        val registrationTokens = Arrays.asList(
            "YOUR_REGISTRATION_TOKEN_1",  // ...
            "YOUR_REGISTRATION_TOKEN_n"
        )

        for(i in 0 until usuariosSelec.size){
           valores+=usuariosSelec[i].token!!
            if(i != usuariosSelec.size-1)
            valores+=", "
        }
        receivers.put("receivers", valores)
        notificationsRef.setValue(receivers)
    }

    fun setTokenAPIRemote(scope: String, name: String?, context: RegistroActivity){

        try {
            var idToken = GoogleAuthUtil.getToken(context, name, scope)
            if (uid != null) {
                myRef.child(uid).setValue(idToken)
            }
        } catch (e: Exception) {
            Log.w("setTokenAPIRemote_Repositorio", "Exception while getting idToken: $e")
        }

    }

}