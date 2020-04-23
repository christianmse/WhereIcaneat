package com.whereicaneat.ui.push

import android.app.Activity
import android.os.AsyncTask

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whereicaneat.R
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Participacion
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.ui.inicio.InicioFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class PushViewModel
    (private val repositorio: Repositorio) : ViewModel() {

    val participaciones = MutableLiveData<MutableList<Participacion>>()
    val ganadores = MutableLiveData<MutableMap<String, Integer>>()
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

    fun getParticipantesRemote(
        tokenRemitente: String): LiveData<MutableList<Participacion>>{
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

    fun terminarVotacion(
        token: String,
        activity: Activity,
        usuarios: MutableList<Usuario>
    ): MutableLiveData<MutableMap<String, Integer>> {
        try{
            repositorio.terminarVotacion(token).observeForever {
                ganadores.value = it
            }
            val task = terminarNotification(activity)
            val resultTask = task.execute(usuarios).get()
        }catch (e:Exception){
            Log.e("terminarVotacion", e.toString())
        }
        return ganadores
    }



    inner class terminarNotification(private var activity: Activity?) : AsyncTask<List<Usuario>, Void, JSONObject>() {

        var listaTokens: MutableList<String>? = mutableListOf<String>()
        var nombreRemitente = CurrentUser.nombre
        var tokenRemitente = CurrentUser.token


        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: List<Usuario>?): JSONObject {

            params[0]?.forEach {
                var token = (it as Usuario).token
                //if(CurrentUser.token != token) Impide que se lo envie a el mismo
                listaTokens?.add(token!!)
            }

            val url = URL("https://fcm.googleapis.com/fcm/send")
            val con = url.openConnection() as HttpsURLConnection
            con.doOutput = true

            // HTTP request header
            con.setRequestProperty("project_id", activity?.getString(R.string.SENDER_ID))
            con.setRequestProperty("Content-Type", "application/json")
            con.setRequestProperty("Accept", "application/json")
            con.setRequestProperty("Authorization", "key=${activity?.getString(R.string.API_KEY)}")
            con.requestMethod = "POST"


            val notificationId = System.currentTimeMillis().toInt()
            // HTTP request
            val data = JSONObject()
            data.put("operation", "add")
            data.put("notification_key_name", notificationId)
            data.put("registration_ids", JSONArray(listaTokens))
            val notification = JSONObject()
            notification.put("title","Hora de comer")
            notification.put("body", "$nombreRemitente ha cerrado la votación")
            data.put("notification",notification)
            val extraData = JSONObject()
            extraData.put("remitente", tokenRemitente)
            data.put("data", extraData)

            val os = con.outputStream
            os.write(data.toString().toByteArray(charset("UTF-8")))
            con.connect()
            os.close()

            if(con.responseCode == HttpURLConnection.HTTP_OK){
                Log.d("terminarNotification_ASYNC", "HTTP OK")
            }
            else{
                Log.e("terminarNotification_ASYNC", "error request, error code:${con.responseCode}")
            }

            // Read the response into a string
            val `is` = con.inputStream
            val responseString = Scanner(`is`, "UTF-8").useDelimiter("\\A").next()
            `is`.close()

            // Parse the JSON string and return the notification key
            return JSONObject(responseString)

        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            //El token es la clave para enlazar notificacion
        }
    }


}