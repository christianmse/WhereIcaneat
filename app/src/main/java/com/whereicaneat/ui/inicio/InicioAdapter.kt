package com.whereicaneat.ui.inicio

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemInvitadosBinding
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.item_invitados.view.*

class InicioAdapter(
    private val usuarios: List<Usuario>,
    private val context: Context
): RecyclerView.Adapter<InicioAdapter.inicioViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        inicioViewHolder(
            DataBindingUtil.inflate<ItemInvitadosBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_invitados,
                parent,
                false
            )
        )

    override fun getItemCount() = usuarios.size

    override fun onBindViewHolder(holder: inicioViewHolder, position: Int) {
        holder.recycler_item_invitados.usuarioModel = usuarios[position]
        holder.bindView(usuarios[position])


    }



    inner class inicioViewHolder(
        val recycler_item_invitados: ItemInvitadosBinding
    ): RecyclerView.ViewHolder(recycler_item_invitados.root){
        fun bindView(usuario: Usuario){
            Glide
                .with(context)
                .load(usuario.imageUri)
                .into(itemView.img_invitado)
            itemView.txt_nombre.text = usuario.nombreUsuario
        }
    }
}