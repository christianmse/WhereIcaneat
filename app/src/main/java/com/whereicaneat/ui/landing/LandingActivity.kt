package com.whereicaneat.ui.landing

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.inicio.InicioActivity
import com.whereicaneat.ui.votacion.VotadoActivity
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_landing.*
import org.json.JSONArray
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LandingActivity : AppCompatActivity(), KodeinAware, RecyclerViewClickListener {
    override val kodein by kodein()
    private val factory: LandingViewModelFactory by instance()
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var adapter:LandingAdapter
    lateinit var restaurantesSelected: List<Restaurante>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        landingViewModel =
            ViewModelProviders.of(this, factory).get(LandingViewModel::class.java)
        adapter = LandingAdapter(this, this)
        recyclerLanding.layoutManager = LinearLayoutManager(this)
        recyclerLanding.adapter = adapter
        Log.e("intent_en_landing", intent.extras.toString())

        if(intent?.getSerializableExtra("restaurantes") != null){
            observarData2(landingViewModel)
        }
        else{
            observarData(landingViewModel)
        }


        landingViewModel.login()
        landingViewModel.setCurrentUser()
    }



    private fun observarData2(viewModel: LandingViewModel) {

        val mutableList: MutableList<String>? = mutableListOf<String>()
        val definitivo: MutableList<Restaurante>? = mutableListOf<Restaurante>()

        //En caso de recibir la notificacion
        val json = intent.getStringExtra("restaurantes")
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            var aux = jsonArray.get(i)
            mutableList?.add(aux.toString())
        }


        btn_votado.visibility = View.VISIBLE
        btn_crear_encuesta.visibility = View.GONE

        shimmer_view_container.startShimmer()
        viewModel.getRestaurantesData().observe(this, Observer {
            shimmer_view_container.stopShimmer()
            shimmer_view_container.hideShimmer()
            shimmer_view_container.visibility = View.GONE

            it.forEach {a->
                mutableList?.forEach {b->
                    if(a.nombre.equals(b)){
                        definitivo?.add(a)
                    }
                }
            }
            adapter.setListData(definitivo!!)
            adapter.notifyDataSetChanged()
    })

    }


    override fun onStart() {
        super.onStart()


        btn_votado.setOnClickListener {
            setRestaurantesSeleccionados()
            if(restaurantesSelected.size > 0){
                val i = Intent(this, VotadoActivity::class.java)
                val remitente = intent.getStringExtra("remitente")
                i.putExtra("remitente", remitente)
                i.putExtra("restaurantesSelec", restaurantesSelected.toTypedArray())
                startActivity(i)
            }
            else{
                tostada("Vota algún restaurante")
            }
        }

        btn_crear_encuesta.setOnClickListener {
            setRestaurantesSeleccionados()
            if(restaurantesSelected.size > 0){
                val i = Intent(this, InicioActivity::class.java)
                i.putExtra("restaurantesSelec", restaurantesSelected.toTypedArray())
                startActivity(i)
            }
            else{
                tostada("Selecciona un restaurante")
            }

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

    override fun onRecyclerViewSearchClick(nombreRestaurante: String) {
        val i = Intent(Intent.ACTION_WEB_SEARCH)
        i.putExtra(SearchManager.QUERY, "Valoraciones de ${nombreRestaurante}")
        startActivity(i)
    }

    override fun onRecyclerMapClick(nombre: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRecyclerCallClick(nombre: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onItemClick(it: View?, restaurante: Restaurante, position: Int) {
        adapter.toggleSelection(position)
    }


}
