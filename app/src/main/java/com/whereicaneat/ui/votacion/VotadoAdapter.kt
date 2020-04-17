package com.whereicaneat.ui.votacion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.android.synthetic.main.item_restaurante.view.*
import kotlinx.android.synthetic.main.item_restaurante.view.imagen

class VotadoAdapter(
    val context: Context
): RecyclerView.Adapter<VotadoAdapter.Holder>() {
    private var restaurantesList = mutableListOf<Restaurante>()

    inner class Holder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        fun bindView(restaurante: Restaurante){
            Glide
                .with(context)
                .load(restaurante.imageUri)
                .into(itemView.imagen)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_votado, parent, false))

    override fun getItemCount(): Int = restaurantesList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(restaurantesList[position])
    }

    fun setData(restaurantesList: MutableList<Restaurante>) {
        this.restaurantesList = restaurantesList
    }
}