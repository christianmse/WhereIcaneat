package com.whereicaneat.ui.resultado

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.landing.RecyclerViewClickListener
import kotlinx.android.synthetic.main.item_restaurante.view.btn_search
import kotlinx.android.synthetic.main.item_restaurante.view.imagen
import kotlinx.android.synthetic.main.item_restaurante.view.nombre_restaurante
import kotlinx.android.synthetic.main.item_resultado_restaurante.view.*

class ResultadoAdapter (
    private val context: Context,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<ResultadoAdapter.ResultadoViewHolder>(){

    private var restaurantesList = mutableListOf<Restaurante>()
    private var restaurantesVotos = mutableListOf<String>()


    fun setListData(
        listData: MutableList<Restaurante>,
        votos: Array<String>?
    ){
        restaurantesList = listData
        votos?.forEach {
            restaurantesVotos.add(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder =
        ResultadoViewHolder(
            LayoutInflater.from(parent.context).
                inflate(R.layout.item_resultado_restaurante, parent, false))


    override fun getItemCount(): Int = restaurantesList.size

    override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int) {
        val restaurante: Restaurante = restaurantesList[position]
        var votos = restaurantesVotos[position]

        holder.bindView(restaurante, votos)
        //si quiero que sea el holder entero pongo holder.root
        holder.itemView.btn_website2.setOnClickListener {
            listener.onRecyclerViewCartaClick(holder.itemView,
                restaurante)
        }
        holder.itemView.btn_search.setOnClickListener {
            listener.onRecyclerViewSearchClick(restaurante.nombre!!)
        }
        holder.itemView.btn_map.setOnClickListener {
            listener.onRecyclerMapClick(restaurante.nombre)
        }


    }






    inner class ResultadoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        fun bindView(
            restaurante: Restaurante,
            votos: String
        ){
            Glide
                .with(context)
                .load(restaurante.imageUri)
                .into(itemView.imagen)
            itemView.nombre_restaurante.text = restaurante.nombre
            itemView.votos.text = votos
        }
    }



}