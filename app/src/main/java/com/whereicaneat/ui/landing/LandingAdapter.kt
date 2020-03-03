package com.whereicaneat.ui.landing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemRestauranteBinding
import com.whereicaneat.domain.data.db.entities.restaurante
import kotlinx.android.synthetic.main.item_restaurante.view.*

class LandingAdapter(
    private val context: Context,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<LandingAdapter.landingViewHolder>(){

    private var restaurantesList = mutableListOf<restaurante>()


    fun setListData(listData: MutableList<restaurante>){
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
        if(restaurantesList.size > 0 )
            return restaurantesList.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: landingViewHolder, position: Int) {
        val restaurante: restaurante = restaurantesList[position]
        holder.itemRestaurantes.restaurante = restaurante
        holder.bindView(restaurante)
        //si quiero que sea el holder entero pongo holder.root
        holder.itemRestaurantes.btnWebsite.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.itemRestaurantes.btnWebsite,
                restaurante)
        }
    }

    inner class landingViewHolder(
        val itemRestaurantes: ItemRestauranteBinding
    ): RecyclerView.ViewHolder(itemRestaurantes.root){
        fun bindView(restaurante: restaurante){
            Glide.with(context).load(restaurante.imageUri).into(itemView.imagen)
            itemView.nombre_restaurante.text = restaurante.nombre
        }
    }

}