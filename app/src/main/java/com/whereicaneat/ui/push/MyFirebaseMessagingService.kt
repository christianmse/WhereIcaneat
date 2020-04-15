package com.whereicaneat.ui.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.whereicaneat.R
import com.whereicaneat.ui.landing.LandingActivity


class MyFirebaseMessagingService: FirebaseMessagingService() {



    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val CHANNEL_ID = "WhereIcanEat"
        val CHANNEL_NAME = "Invitados"

        //Recuperamos el body de la notificacion
        var title = p0.notification?.title
        var body = p0.notification?.body
        //Recuperamos la extra data de la info
        var extraData: Map<String,String> = p0.data
        var restaurante = extraData.get("restaurantes")
        var remitente = extraData.get("remitente")

        val i = Intent(this, LandingActivity::class.java)
        i.putExtra("restaurantes", restaurante)
        i.putExtra("remitente", remitente)

        val pendingIntent= PendingIntent.getActivity(this,10,i,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setFullScreenIntent(pendingIntent, true)
            .setVisibility(VISIBILITY_PUBLIC)


        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = System.currentTimeMillis().toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId,notificationBuilder.build());
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.e("new token", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }
}