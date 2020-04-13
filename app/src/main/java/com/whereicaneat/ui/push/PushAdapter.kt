package com.whereicaneat.ui.push

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.size
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.databinding.ItemRestauranteBinding
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.ui.inicio.InicioAdapter
import kotlinx.android.synthetic.main.item_invitados.view.*
import kotlinx.android.synthetic.main.item_restaurante.view.*


class PushAdapter (
    private val context: Context
) : RecyclerView.Adapter<PushAdapter.Holder>() {
    private var restaurantesList = mutableListOf<Restaurante>()


    fun setListData(listData: MutableList<Restaurante>){
        restaurantesList = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(
            DataBindingUtil.inflate<ItemRestauranteBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_restaurante,
                parent,
                false
            )
        )

    override fun getItemCount() = restaurantesList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val restaurante: Restaurante = restaurantesList[position]
        holder.itemRestaurantes.restauranteVar = restaurante
        holder.bindView(restaurante)
    }

    inner class Holder(
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
