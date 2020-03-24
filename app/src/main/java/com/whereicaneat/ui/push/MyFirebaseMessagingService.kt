package com.whereicaneat.ui.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.whereicaneat.R


class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        //Recuperamos el body de la notificacion
        var title = p0.notification?.title
        var body = p0.notification?.body
        //Recuperamos la extra data de la info
       var extraData: Map<String,String> = p0.data
        var restaurante = extraData.get("restaurantes")

        val notificationBuilder = NotificationCompat.Builder(this,"TAC")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.logo)

        val i = Intent(this, ReceivedNotification::class.java)
        i.putExtra("restaurantes", restaurante)

        val pendingIntent= PendingIntent.getActivity(this,10,i,
            PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder.setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = System.currentTimeMillis().toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("TAC", "demo", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id,notificationBuilder.build());
    }
}