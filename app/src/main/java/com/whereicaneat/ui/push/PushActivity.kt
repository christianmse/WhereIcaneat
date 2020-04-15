package com.whereicaneat.ui.push

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.whereicaneat.R
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.domain.data.db.entities.Restaurante
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
    //lateinit var adapter:PushAdapter
    val listaRestaurantes = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)
        viewModel = ViewModelProviders.of(this, factory).get(PushViewModel::class.java)
       // adapter = PushAdapter(this)
        restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        usuariosSelec = intent.getParcelableArrayExtra("usuariosSelec")
       // recycler_push.adapter = adapter
        recycler_push.layoutManager = LinearLayoutManager(this)
        getRestaurantesParcelable(restaurantesSelec)



        /*viewModel.getParticipantesRemote(CurrentUser.token)
        viewModel.participaciones.observeForever {participacionesList ->
            adapter.setListData(participacionesList)
            adapter.notifyDataSetChanged()
        }*/
        /*viewModel.getRestauranteCount(restaurante!!).observeForever {
            it.forEach {
                gente.text = it.nombreUsuario
            }
        }*/
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
