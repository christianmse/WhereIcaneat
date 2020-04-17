package com.whereicaneat.ui.push

import android.app.IntentService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.landing.LandingActivity
import com.whereicaneat.ui.landing.LandingViewModel
import com.whereicaneat.ui.landing.LandingViewModelFactory
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.activity_received_notification.*
import kotlinx.android.synthetic.main.item_invitados.*
import org.json.JSONArray
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception


class ReceivedNotification : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_notification)

        txt_nombre.text = intent?.getSerializableExtra("restaurantes").toString()
        }

}

