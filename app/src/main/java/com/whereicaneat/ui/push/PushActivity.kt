package com.whereicaneat.ui.push

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.android.synthetic.main.activity_push.*
import org.json.JSONException
import org.json.JSONObject


class PushActivity : AppCompatActivity() {
    val URL = "https://fcm.googleapis.com/fcm/send"
    lateinit var mRequestQueue: RequestQueue
    lateinit var restaurantesSelected: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)
        val restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        val usuariosSelec = intent.getParcelableArrayExtra("usuariosSelec")
        mRequestQueue = Volley.newRequestQueue(this)
        FirebaseMessaging.getInstance().subscribeToTopic("news")
        btn_push.setOnClickListener {
            sendNotification()
        }
    }


    fun setExtraData(restaurantes: JSONObject){
        restaurantesSelected = restaurantes
    }

    fun sendNotification(){
        val json:JSONObject = JSONObject()
        try{
            json.put("to", "/topics/"+"news")
            var notificationObj: JSONObject = JSONObject()
            notificationObj.put("title", "Hora de Comer!")
            notificationObj.put("body", "Vota tu restaurante favorito")
            json.put("notification", notificationObj)

            //ExtraData
//            if(restaurantesSelected != null){
//                val extraData:JSONObject = JSONObject()
//                //Pasar un Map de restaurantes
//                val data = intent.getStringExtra("restaurantes") //debe aparecer el macas
//                extraData.put("restaurantes", data)
//                json.put("data", restaurantesSelected)
//            }

            val request = object : JsonObjectRequest(Request.Method.POST, URL, json,
                Response.Listener { response ->
                    if (response != null) {
                        Log.d("MUR", "onResponse")
                    }
                }, Response.ErrorListener { error ->
                    error.printStackTrace() }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    var header: MutableMap<String, String> = HashMap()
                    header.put("content-type", "application/json")
                    header.put("authorization", "key=AIzaSyCi_-Ho9MEhsVy80acfTZkynx5lMZezLVs")
                    return header
                }
            }
            mRequestQueue.add(request)
        }catch (e: JSONException){
            e.printStackTrace()
        }

    }
}
