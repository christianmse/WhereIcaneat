package com.whereicaneat.ui.push

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Participacion
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.ui.inicio.InicioFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class PushViewModel
    (private val repositorio: Repositorio) : ViewModel() {


    val mutableData = MutableLiveData<MutableList<Restaurante>>()

    //Obtiene restaurantes de firestore
    fun getRestaurantesData():LiveData<MutableList<Restaurante>>{
        repositorio.getRestaurantesRemote().observeForever {
            mutableData.value = it
        }


        return mutableData
    }

    fun getRestauranteCount(restaurante: String): MutableLiveData<List<Usuario>> {
        var contador= MutableLiveData<List<Usuario>>()
        //Veo que el asynctask haya terminado
        InicioFragment.getStatusTask().observeForever { estadoAsyncTask ->
            if (estadoAsyncTask) {

                val usuarioPrueba = Usuario(null, "prueba", "999999", "token", "uid")

                FirebaseDatabase.getInstance()
                    .getReference("Notifications")
                    .child(CurrentUser.token)
                    .child(restaurante)
                    .setValue(usuarioPrueba)


                FirebaseDatabase.getInstance()
                    .getReference("Notifications")
                    .child(CurrentUser.token)
                    .child(restaurante)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("11111", p0.message)
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.childrenCount > 0) {

                               val invitados = mutableListOf<Usuario>()
                                for(participanteSnapshot in dataSnapshot.children){
                                    val participante = participanteSnapshot.getValue(Usuario::class.java)
                                    participante?.let {invitados.add(participante)}
                                }
                                contador.value = invitados
                            }
                        }
                    }
                    )
            }

        }
        return contador

    }

    val participaciones = MutableLiveData<MutableList<Participacion>>()
    fun getParticipantesRemote(
        tokenRemitente: String
    ): LiveData<MutableList<Participacion>>{
        CoroutineScope(Dispatchers.Main).launch{
            try {
                repositorio.getParticipantesRemote(tokenRemitente).observeForever {
                    participaciones.value = it
                }
            }catch (e: Exception){
                Log.e("getParticipantesRemoteVM", e.toString())
            }
        }
        return participaciones
    }


}