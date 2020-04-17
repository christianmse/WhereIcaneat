package com.whereicaneat.ui.landing

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemRestauranteBinding
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.ui.inicio.InicioAdapter
import kotlinx.android.synthetic.main.item_restaurante.view.*

class LandingAdapter(
    private val context: Context,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<LandingAdapter.landingViewHolder>(){

    var selected_items = SparseBooleanArray()
    var current_selected_idx = -1
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
        holder.itemRestaurantes.lytDad.isActivated = selected_items.get(position,false)
        holder.itemRestaurantes.tapLayout.setOnClickListener {
            if(listener != null){
                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(it,restaurante,position)
                }
            }


        }
        toggleCheckedIcon(holder, position)
    }

    private fun toggleCheckedIcon(holder: LandingAdapter.landingViewHolder, position: Int) {
        if (selected_items[position, false]) {
            holder.itemView.lyt_image.setVisibility(View.GONE)
            holder.itemView.lyt_checked.setVisibility(View.VISIBLE)
            if (current_selected_idx == position) resetCurrentIndex()
        } else {
            holder.itemView.lyt_checked.setVisibility(View.GONE)
            holder.itemView.lyt_image.setVisibility(View.VISIBLE)
            if (current_selected_idx == position) resetCurrentIndex()
        }
    }

    private fun resetCurrentIndex() {
        current_selected_idx = -1
    }

    fun getSelectedItems(): List<Restaurante>? {
        var items: MutableList<Restaurante> = mutableListOf<Restaurante>()
        selected_items.forEach { key, value ->
            items.add(restaurantesList[key])
        }
        return items
    }

    fun toggleSelection(pos: Int) {
        current_selected_idx = pos
        if (selected_items[pos, false]) {
            selected_items.delete(pos)
        } else {
            selected_items.put(pos, true)
        }
        notifyItemChanged(pos)
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