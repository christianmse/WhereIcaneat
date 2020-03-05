package com.whereicaneat.ui.inicio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Movie
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.R
import com.whereicaneat.databinding.ItemInvitadosBinding
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.item_invitados.view.*


class InicioAdapter(
    private val context: Context
) : RecyclerView.Adapter<InicioAdapter.inicioViewHolder>() {

    private var usuarios: List<Usuario> = mutableListOf()

    fun setListData(data: List<Usuario>) {
        usuarios = data

    }

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
        Log.e("InicioAdapter", "Datos${usuarios[position].nombreUsuario}")
        val usuarioAux: Usuario = usuarios[position]
        holder.recycler_item_invitados.usuarioModel = usuarioAux
        holder.bindView(usuarioAux)


    }


    inner class inicioViewHolder(
        val recycler_item_invitados: ItemInvitadosBinding
    ) : RecyclerView.ViewHolder(recycler_item_invitados.root) {
        fun bindView(usuario: Usuario) {

            val storageReference = FirebaseStorage
                .getInstance()
                .getReference("Imagenes_Perfil")
                .child(usuario.telefono)

                storageReference
                    .getBytes(1024*1024)
                    .addOnSuccessListener {
                        var bitmap: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        itemView.img_invitado.setImageBitmap(bitmap)
                    }


//
//            storageReference.downloadUrl
//                .addOnSuccessListener {
//                    Log.e("url1111111", it.toString())
//                }

//            context.let {
//                Glide
//                    .with(it)
//                    .load(storageReference)
//                    .into(itemView.img_invitado)
//                itemView.txt_nombre.text = usuario.nombreUsuario
//
//            }
        }


    }

}


