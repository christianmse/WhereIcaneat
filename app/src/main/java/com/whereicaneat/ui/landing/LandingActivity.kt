package com.whereicaneat.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import kotlinx.android.synthetic.main.activity_landing.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class LandingActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    //private val factory: RegistroViewModelFactory by instance()


    private lateinit var adapter:LandingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val database = DatabaseLocal(applicationContext)
        val repository = Repositorio(database)
        val factory = LandingViewModelFactory(repository)
        val landingViewModel =
            ViewModelProviders.of(this, factory).get(LandingViewModel::class.java)

        adapter = LandingAdapter(this)
        recyclerLanding.layoutManager = LinearLayoutManager(this)
        recyclerLanding.adapter = adapter
        observarData(landingViewModel)

    }

    fun observarData(viewModel: LandingViewModel){
        shimmer_view_container.startShimmer()
        viewModel.fetchUserData().observe(this, Observer {
            shimmer_view_container.stopShimmer()
            shimmer_view_container.hideShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}
