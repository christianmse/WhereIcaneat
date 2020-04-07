package com.whereicaneat.domain.data.remote

import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.http.Field
import retrofit2.http.Header

interface FCM {

    @FormUrlEncoded
    @POST("notification")
    fun crearGrupo(
        @Header("Content-Type") Content_Type:String,
        @Header("Authorization") api_key:String,
        @Header("project_id") sender_id:String,
    @Field("operation") operation:String,
    @Field("notification_key_name") notification_key_name:String,
    @Field("registration_ids") lista_tokens:JSONArray

    ): Call<ResponseBody>
}

object ApiUtils {

    val BASE_URL = "https://fcm.googleapis.com/fcm/"

    val apiService: FCM
        get() = RetrofitClient.getClient(BASE_URL)!!.create(FCM::class.java)

}