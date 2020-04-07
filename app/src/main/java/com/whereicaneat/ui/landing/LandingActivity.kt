package com.whereicaneat.ui.landing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.inicio.InicioActivity
import kotlinx.android.synthetic.main.activity_landing.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LandingActivity : AppCompatActivity(), KodeinAware, RecyclerViewClickListener {
    override val kodein by kodein()
    private val factory: LandingViewModelFactory by instance()
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var adapter:LandingAdapter
    private lateinit var listRestaurante: List<Restaurante>
    lateinit var restaurantesSelected: List<Restaurante>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        landingViewModel =
            ViewModelProviders.of(this, factory).get(LandingViewModel::class.java)
        adapter = LandingAdapter(this, this)
        recyclerLanding.layoutManager = LinearLayoutManager(this)
        recyclerLanding.adapter = adapter
        observarData(landingViewModel)

        btn_crear_encuesta.setOnClickListener {
            setRestaurantesSeleccionados()
            val i = Intent(this, InicioActivity::class.java)
            i.putExtra("restaurantesSelec", restaurantesSelected.toTypedArray())
            startActivity(i)
        }
    }

    override fun onBackPressed() { // Do Here what ever you want do on back press;
    }

    fun setRestaurantesSeleccionados(){
        restaurantesSelected = adapter.getSelectedItems()!!
    }

    fun observarData(viewModel: LandingViewModel){

        shimmer_view_container.startShimmer()
        viewModel.getRestaurantesData().observe(this, Observer {
            listRestaurante = it
            shimmer_view_container.stopShimmer()
            shimmer_view_container.hideShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onRecyclerViewCartaClick(view: View, restaurante: Restaurante) {
        val url = restaurante.website
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun setOnSelectedRestaurante(view: View?, obj:Restaurante?, position: Int) {
        listRestaurante[position]

        btn_crear_encuesta.visibility = View.VISIBLE
    }

    override fun onItemClick(it: View?, restaurante: Restaurante, position: Int) {
        adapter.toggleSelection(position)
    }


}
