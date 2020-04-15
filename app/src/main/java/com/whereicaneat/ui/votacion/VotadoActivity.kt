package com.whereicaneat.ui.votacion

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import com.whereicaneat.R
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.android.synthetic.main.activity_votado.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class VotadoActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by kodein()
    val factory : VotadoViewModelFactory by instance()
    val restaurantesList = mutableListOf<Restaurante>()
    lateinit var remitente: String
    lateinit var adapter: VotadoAdapter
    lateinit var viewModel: VotadoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votado)
        viewModel = ViewModelProviders.of(this, factory).get(VotadoViewModel::class.java)
        val restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        remitente = intent.getStringExtra("remitente")
        getRestaurantes(restaurantesSelec)
        adapter = VotadoAdapter(this)
        adapter.setData(restaurantesList)
        recycler_votado.layoutManager = LinearLayoutManager(this)
        recycler_votado.adapter = adapter
    }

    private fun getRestaurantes(restaurantesSelec: Array<Parcelable>?) {
        restaurantesSelec?.forEach {
            var restaurante = (it as Restaurante)
            restaurantesList.add(restaurante)
        }
        viewModel.sendVoto(CurrentUser.token, restaurantesList, remitente)
    }

    override fun onStart() {
        super.onStart()
        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(200.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
    }


}
