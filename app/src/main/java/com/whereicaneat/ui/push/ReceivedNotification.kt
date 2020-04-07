package com.whereicaneat.ui.push

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.whereicaneat.R
import kotlinx.android.synthetic.main.activity_received_notification.*

class ReceivedNotification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_notification)

        if(intent.hasExtra("saludo")){
            push_restaurantes.text = intent.getStringExtra("restaurantes")
        }
    }
}
