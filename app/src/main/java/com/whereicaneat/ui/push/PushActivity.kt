package com.whereicaneat.ui.push

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.resultado.ResultadoActivity
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
            val intent = Intent(this, ResultadoActivity::class.java)
            val listRest = mutableListOf<String>()
            val listCont = mutableListOf<Integer>()
            viewModel.terminarVotacion(CurrentUser.token).observe(this, Observer { ganadores ->

                ganadores.forEach {
                    listRest.add(it.key)
                    listCont.add(it.value)
                }
                intent.putExtra("ganadoresRestaurantes", listRest.toTypedArray())
                intent.putExtra("ganadoresContador", listCont.toTypedArray())
                startActivity(intent)
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
