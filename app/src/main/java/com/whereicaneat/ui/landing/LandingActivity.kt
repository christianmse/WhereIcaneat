package com.whereicaneat.ui.landing

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.inicio.InicioActivity
import com.whereicaneat.ui.inicio.InicioFragment
import kotlinx.android.synthetic.main.activity_landing.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class LandingActivity : AppCompatActivity(), KodeinAware, RecyclerViewClickListener {
    override val kodein by kodein()
    private val factory: LandingViewModelFactory by instance()
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var adapter:LandingAdapter
    private lateinit var listRestaurante: List<Restaurante>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        landingViewModel =
            ViewModelProviders.of(this, factory).get(LandingViewModel::class.java)

        adapter = LandingAdapter(this, this)
        recyclerLanding.layoutManager = LinearLayoutManager(this)
        recyclerLanding.adapter = adapter
        observarData(landingViewModel)

        val fragment = InicioFragment()
        btn_crear_encuesta.setOnClickListener {
            try {
                startActivity(Intent(this, InicioActivity::class.java))

            }catch (e:Exception){
                Log.e("1111111","fallo reemplazar fragment${e.toString()}")
            }

        }



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

    override fun setOnSelectedRestaurante(position: Int) {
        listRestaurante[position]
        btn_crear_encuesta.visibility = View.VISIBLE
    }


}
