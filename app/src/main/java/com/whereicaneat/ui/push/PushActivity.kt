package com.whereicaneat.ui.push

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_push.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class PushActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: PushViewModelFactory by instance()
    lateinit var viewModel: PushViewModel
    lateinit var restaurantesSelec: Array<Parcelable>
    lateinit var usuariosSelec: Array<Parcelable>
    lateinit var adapter:PushAdapter
    val listaRestaurantes = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)
        viewModel = ViewModelProviders.of(this, factory).get(PushViewModel::class.java)
       adapter = PushAdapter(this)
        restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        usuariosSelec = intent.getParcelableArrayExtra("usuariosSelec")
       recycler_push.adapter = adapter
        recycler_push.layoutManager = LinearLayoutManager(this)
        getRestaurantesParcelable(restaurantesSelec)



        viewModel.getParticipantesRemote(CurrentUser.token)
        viewModel.participaciones.observe(this, Observer {participacionesList ->
                adapter.setListData(participacionesList)
                adapter.notifyDataSetChanged()

        })

    }

    override fun onStart() {
        super.onStart()
        btn_terminar.setOnClickListener {
            viewModel.terminarVotacion(CurrentUser.token).observe(this, Observer { ganadores ->
                tostada(ganadores.toString())
            })

        }
    }


    fun getRestaurantesParcelable(restaurantesSelec: Array<Parcelable>?){
        restaurantesSelec?.forEach {
            var aux = (it as Restaurante).nombre
            if (aux != null) {
                listaRestaurantes.add(aux)
            }
        }
    }

}
