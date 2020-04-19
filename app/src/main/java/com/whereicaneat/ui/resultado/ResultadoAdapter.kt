package com.whereicaneat.ui.resultado

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemResultadoRestauranteBinding
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.landing.RecyclerViewClickListener
import kotlinx.android.synthetic.main.item_restaurante.view.*

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder =
        ResultadoViewHolder(
            LayoutInflater.from(parent.context).
                inflate(R.layout.item_resultado_restaurante, parent, false))


    override fun getItemCount(): Int = restaurantesList.size

    override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int) {
        val restaurante: Restaurante = restaurantesList[position]

        holder.bindView(restaurante)
        //si quiero que sea el holder entero pongo holder.root
        /*holder.itemRestaurantes.btnWebsite.setOnClickListener {
            listener.onRecyclerViewCartaClick(holder.itemRestaurantes.btnWebsite,
                restaurante)
        }
        holder.itemRestaurantes.btnSearch.setOnClickListener {
            listener.onRecyclerViewSearchClick(restaurante.nombre!!)
        }*/


    }






    inner class ResultadoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
        fun bindView(restaurante: Restaurante){
            Glide
                .with(context)
                .load(restaurante.imageUri)
                .into(itemView.imagen)
            itemView.nombre_restaurante.text = restaurante.nombre
        }
    }



}