package com.whereicaneat.ui.push

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
import org.json.JSONArray
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception


class ReceivedNotification : AppCompatActivity() {
    lateinit var database:DatabaseLocal
    lateinit var repository:Repositorio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_notification)

        database = DatabaseLocal(applicationContext)
        repository = Repositorio(database)
        val adapter = PushAdapter(this)
        recycler_received.layoutManager = LinearLayoutManager(this)
        recycler_received.adapter = adapter

        if(intent.hasExtra("restaurantes")){
            val json = intent.getStringExtra("restaurantes")
            val jsonArray = JSONArray(json)
            val mutableList: MutableList<String>? = mutableListOf<String>()
            for (i in 0 until jsonArray.length()){
                var aux = jsonArray.get(i)
                mutableList?.add(aux.toString())
            }

            remitente.text = intent.getStringExtra("remitente")

           /* lateinit var listRestaurante: List<Restaurante>
            val vm = LandingViewModel(repository)
            try {
                vm.getRestaurantesData().observe(this, Observer {
                    listRestaurante = it
                })
            }catch (e:Exception){
                Log.e("received", e.toString())
            }

            adapter.setListData(listRestaurante.toMutableList())*/




            }
        }
    }

