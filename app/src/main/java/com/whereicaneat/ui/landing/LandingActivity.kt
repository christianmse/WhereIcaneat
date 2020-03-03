package com.whereicaneat.ui.landing

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.restaurante
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LandingActivity : AppCompatActivity(), KodeinAware, RecyclerViewClickListener {
    override val kodein by kodein()
    private val factory: LandingViewModelFactory by instance()
    private lateinit var landingViewModel: LandingViewModel
    private lateinit var adapter:LandingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        landingViewModel =
            ViewModelProviders.of(this, factory).get(LandingViewModel::class.java)

        adapter = LandingAdapter(this, this)
        recyclerLanding.layoutManager = LinearLayoutManager(this)
        recyclerLanding.adapter = adapter
        observarData(landingViewModel)



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

    override fun onRecyclerViewItemClick(view: View, restaurante: restaurante) {
        val url = restaurante.website
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }


}
