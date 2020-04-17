package com.whereicaneat.ui.push

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.item_participacion2.view.*

class SubItemAdapter(val participantes: MutableList<Usuario>): RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        SubItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_participacion2, parent, false))

    override fun getItemCount(): Int {
        return participantes.size
    }

    override fun onBindViewHolder(holder: SubItemViewHolder, position: Int) {
        val usuario = participantes[position]
        holder.bindView(usuario)
    }







    inner class SubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(usuario: Usuario){
            itemView.tv_sub_item_title.text = usuario.nombreUsuario
            Glide
                .with(itemView.context)
                .load(usuario.imageUri)
                .into(itemView.img_sub_item)
        }
    }



}
