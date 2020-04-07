package com.whereicaneat.ui.push

import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.activity_push.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class PushActivity : AppCompatActivity() {

    val URL = "https://fcm.googleapis.com/fcm/send"
    val URL_GROUP = "https://fcm.googleapis.com/fcm/googlenotification"
    val API_KEY = "AAAAAI2GKWY:APA91bFCi1xqZC7cziz9OxeYjIivDCr8fRzmQA4uAT651ZZTdDHbAkrfrQMRDM_bKlzPkxL1LMp4yKvt74tEWsb53EhmbYuCDSKvGDdnvgB7_hjAFUcPWS6HgucS6g5I4rPYZtx4zvAb"
    val sender_id = "2374379878"

    lateinit var mRequestQueue: RequestQueue
    lateinit var mRequestGroup: RequestQueue
    lateinit var restaurantesSelected: JSONObject
    lateinit var restaurantesSelec: Array<Parcelable>
    lateinit var usuariosSelec: Array<Parcelable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)
        restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        usuariosSelec = intent.getParcelableArrayExtra("usuariosSelec")
        var response = intent.getStringExtra("response")
        responsetxt.text = response

        mRequestQueue = Volley.newRequestQueue(this)
        mRequestGroup = Volley.newRequestQueue(this)
        FirebaseMessaging.getInstance().subscribeToTopic("news")

        btn_push.setOnClickListener {
            //createGroup()
            //sendNotification()

        }


    }

    fun createGroup(){
        val json:JSONObject = JSONObject()
        val lista: List<String> = listOf("AVSRccrOfrVcITKo9QFBJUE8wHm2")
        json.put("operation", "add")
        json.put("notification_key_name","christianmsespinoza@gmail.com" )
        json.put("registration_ids", JSONArray(arrayListOf("digieLCVQ_S8If166_SCRf:APA91bFU1wM_nriWUhhEDMYzE92exrr-rfZTdnEH1sFQ853oU-0j4hftkD0PlnPlvoL5eX2Md5cq6lyMYCXNzaAk9xWCMAK_fZmVyukaoKcq-m-045qxFrjHa-3WPAsZpVDSJHdNxfjx")));
        json.put("id_token", "digieLCVQ_S8If166_SCRf:APA91bFU1wM_nriWUhhEDMYzE92exrr-rfZTdnEH1sFQ853oU-0j4hftkD0PlnPlvoL5eX2Md5cq6lyMYCXNzaAk9xWCMAK_fZmVyukaoKcq-m-045qxFrjHa-3WPAsZpVDSJHdNxfjx")

        try {
            val request = object : JsonObjectRequest(Request.Method.POST, URL_GROUP, json,
                Response.Listener { response ->
                    if (response != null) {
                        Log.e("CreateGroup", "onResponse "+ response.getString("successMessage"))
                        if (response.getBoolean("resultFlag"))
                            Log.e("todoOk", response.getString("successMessage"))
                    } else {
                       Log.e("response a nulo", response?.getString("errorMessage"))
                    }


                }, Response.ErrorListener { error ->
                    var jerror:JSONObject = JSONObject()
                    Log.e("CreateGroup", "onError "+error.toString())
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    var header: MutableMap<String, String> = HashMap()
                    header.put("content-type", "application/json")
                    header.put("Accept", "application/json")
                    //header.put("authorization", "key="+API_KEY)
                    header.put("project_id",sender_id)
                    return header
                }
            }

            mRequestGroup.add(request)



        }catch (e:Exception){
            Log.e("CreateGroupError", e.toString())
        }

    }

    suspend  fun setExtraData(restaurantes: JSONObject){
        restaurantesSelected = restaurantes
    }

    //Esta es la notificacion que llegará al movil en el servicio
    //MyFirebaseMessagingService metodo onMessageReceived
    fun sendNotification(){
        var jsonArray:JSONArray = JSONArray()
        var json:JSONObject = JSONObject()



            //json.put("to", "/topics/"+"news")
            json.put("to", "toke")
            //json.put("to", "Christian")
            json.put("priority", "high")

            var notificationObj: JSONObject = JSONObject()
            notificationObj.put("title", "Hora de Comer!")
            notificationObj.put("body", "Vota tu restaurante favorito")
            json.put("notification", notificationObj)

            jsonArray.put(0, json)

                try{



                    //ExtraData
//            if(restaurantesSelected != null){
//                val extraData:JSONObject = JSONObject()
//                //Pasar un Map de restaurantes
//                val data = intent.getStringExtra("restaurantes") //debe aparecer el macas
//                extraData.put("restaurantes", data)
//                json.put("data", restaurantesSelected)
//            }


                    val request = object : JsonArrayRequest(Request.Method.POST, URL, jsonArray,
                        Response.Listener { response ->
                            if (response != null) {
                                Log.d("MUR", "onResponse")
                            }
                        }, Response.ErrorListener { error ->
                            Log.e("requestttt",error.toString()) }
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
                    Log.e("send_no", e.toString())
                }



    }




}
