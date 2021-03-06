package com.whereicaneat.ui.resultado

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.landing.RecyclerViewClickListener
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_resultado.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class ResultadoActivity : AppCompatActivity(), KodeinAware, RecyclerViewClickListener {
    override val kodein by kodein()
    private val factory: ResultadoViewModelFactory by instance()
    lateinit var resultadoViewModel: ResultadoViewModel
    private lateinit var adapter: ResultadoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)
        resultadoViewModel = ViewModelProviders.of(this, factory).get(ResultadoViewModel::class.java)
        adapter = ResultadoAdapter(this, this)
        rv_resultado.layoutManager = LinearLayoutManager(this)
        rv_resultado.adapter = adapter

        if(intent.extras != null){
            val restaurantes = intent.getStringArrayExtra("ganadoresRestaurantes")
            val votos = intent.getStringArrayExtra("ganadoresContador")
            observardata(restaurantes, votos)
        }

    }

    private fun observardata(
        restaurantes: Array<String>?,
        votos: Array<String>?
    ) {
        resultadoViewModel.getRestaurantesData(restaurantes).observe(this, Observer {
            adapter.setListData(it, votos)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onRecyclerViewCartaClick(view: View, restaurante: Restaurante) {
        val url = restaurante.website
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onItemClick(it: View?, restaurante: Restaurante, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecyclerViewSearchClick(nombreRestaurante: String) {
        val i = Intent(Intent.ACTION_WEB_SEARCH)
        i.putExtra(SearchManager.QUERY, "Valoraciones de ${nombreRestaurante}")
        startActivity(i)    }

    override fun onRecyclerMapClick(nombre: String?) {

        val gmmIntentUri = Uri.parse("geo:0,0?q=restaurantes cerca de mi")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }

    }

    override fun onRecyclerCallClick(nombre: String?) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:999999999")

        startActivity(callIntent)
    }
}
