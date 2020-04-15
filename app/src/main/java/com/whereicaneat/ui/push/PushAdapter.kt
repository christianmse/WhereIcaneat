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
import com.whereicaneat.databinding.ItemParticipacionBinding
import com.whereicaneat.databinding.ItemRestauranteBinding
import com.whereicaneat.domain.data.db.entities.Participacion
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.ui.inicio.InicioAdapter
import kotlinx.android.synthetic.main.item_invitados.view.*
import kotlinx.android.synthetic.main.item_participacion.view.*
import kotlinx.android.synthetic.main.item_restaurante.view.*


class PushAdapter (
    private val context: Context
) : RecyclerView.Adapter<PushAdapter.Holder>() {
    private var participacionesList = mutableListOf<Participacion>()


    fun setListData(listData: MutableList<Participacion>){
        participacionesList = listData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(
            DataBindingUtil.inflate<ItemParticipacionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_participacion,
                parent,
                false
            )
        )

    override fun getItemCount() = participacionesList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val participacion: Participacion = participacionesList[position]
        holder.itemParticipacionBinding.participacionVar = participacion
        holder.bindView(participacion)
    }

    inner class Holder(
        val itemParticipacionBinding: ItemParticipacionBinding
    ): RecyclerView.ViewHolder(itemParticipacionBinding.root){
        fun bindView(participacion: Participacion){
            /*lateinit var nombres: String
            //Coger la lista de participantes
            participacion.participantes.forEach {usuario ->
                nombres += "${usuario.nombreUsuario}, "
            }
            if(nombres != null){
                itemView.txt_participante.text = nombres
            }
            else{
                itemView.txt_participante.text = "Vacio"
            }*/

        }
    }

}
