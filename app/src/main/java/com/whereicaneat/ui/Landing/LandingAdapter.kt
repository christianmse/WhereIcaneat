package com.whereicaneat.ui.Landing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.whereicaneat.R
import com.whereicaneat.domain.data.db.entities.usuario
import kotlinx.android.synthetic.main.item_invitacion.view.*

class LandingAdapter(private val context: Context)
    : RecyclerView.Adapter<LandingAdapter.viewHolder>(){

    private var usuariosList = mutableListOf<usuario>()

    fun setListData(listData: MutableList<usuario>){
        usuariosList = listData
    }


    inner class viewHolder(vista: View): RecyclerView.ViewHolder(vista){
        fun bindView(usuario: usuario){
            Glide.with(context).load(usuario.imageUrl).into(itemView.avatar_invitacion)
            itemView.nombre_invitacion.text = usuario.nombreUsuario
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.item_invitacion, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        if(usuariosList.size > 0 )
            return usuariosList.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val usuario: usuario = usuariosList[position]
        holder.bindView(usuario)
    }

}