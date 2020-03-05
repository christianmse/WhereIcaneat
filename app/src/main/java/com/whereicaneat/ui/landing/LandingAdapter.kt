package com.whereicaneat.ui.landing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemRestauranteBinding
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.android.synthetic.main.item_restaurante.view.*

class LandingAdapter(
    private val context: Context,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<LandingAdapter.landingViewHolder>(){

    private var restaurantesList = mutableListOf<Restaurante>()


    fun setListData(listData: MutableList<Restaurante>){
        restaurantesList = listData
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        landingViewHolder(
            DataBindingUtil.inflate<ItemRestauranteBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_restaurante,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
            return restaurantesList.size

    }

    override fun onBindViewHolder(holder: landingViewHolder, position: Int) {
        val restaurante: Restaurante = restaurantesList[position]
        holder.itemRestaurantes.restauranteVar = restaurante
        holder.bindView(restaurante)
        //si quiero que sea el holder entero pongo holder.root
        holder.itemRestaurantes.btnWebsite.setOnClickListener {
            listener.onRecyclerViewCartaClick(holder.itemRestaurantes.btnWebsite,
                restaurante)
        }

        holder.itemRestaurantes.nombreRestaurante.setOnClickListener {
            if(listener != null){
                if(position != RecyclerView.NO_POSITION){
                    listener.setOnSelectedRestaurante(position)
                }
            }
        }
    }



    inner class landingViewHolder(
        val itemRestaurantes: ItemRestauranteBinding
    ): RecyclerView.ViewHolder(itemRestaurantes.root){
        fun bindView(restaurante: Restaurante){
            Glide
                .with(context)
                .load(restaurante.imageUri)
                .into(itemView.imagen)
            itemView.nombre_restaurante.text = restaurante.nombre
        }
    }

}