package com.whereicaneat.ui.inicio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Movie
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.databinding.ItemInvitadosBinding
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.item_invitados.view.*


class InicioAdapter(
    private val context: Context,
    val inicioInterface: InicioInterface
) : RecyclerView.Adapter<InicioAdapter.inicioViewHolder>(), ICardItemClickListener {

    private var usuarios: List<Usuario> = mutableListOf()
    val selectedIds: MutableList<String> = ArrayList<String>()

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
            ), this
        )

    override fun getItemCount() = usuarios.size

    override fun onBindViewHolder(holder: inicioViewHolder, position: Int) {
        val usuarioAux: Usuario = usuarios[position]
        holder.recycler_item_invitados.usuarioModel = usuarioAux
        holder.bindView(usuarioAux)
        val id = usuarios[position].telefono

        if(selectedIds.contains(id)){
            //If el item es seleccionado cambio de color el foreground
            holder?.frameLayout?.foreground = ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated))
        } else {
            //else elimina el colorControlActivated
            holder?.frameLayout?.foreground = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
        }


        //Antiguo:
        /*holder.setEvent(object:ICardItemClickListener{
            override fun onItemClicked(vista: View, posicion: Int) {
                context.tostada("Invita a ${usuarios[posicion].nombreUsuario}")
            }

        })*/
    }



    override fun getItemViewType(position: Int): Int {
        return if(usuarios.size == 1)
            1 //Si queda un item poner una columna
        else {
            if (usuarios.size % Common.NUM_OF_COLUMN == 0)
                1
            else
                if(position>1 && position == usuarios.size -1) 0 else 1
        }
    }
    override fun onItemClicked(posicion: Int) {
        if(InicioFragment.isMultiseleccion)
            addIDIntoSelectedIds(posicion)
        else {
            context.tostada("¡${usuarios[posicion].nombreUsuario} invitado!")
        }
    }

    override fun onLongTap(index: Int) {
        if(!InicioFragment.isMultiseleccion){
            InicioFragment.isMultiseleccion = true
        }
        addIDIntoSelectedIds(index)
    }

    private fun addIDIntoSelectedIds(index: Int) {
        val id = usuarios[index].telefono //telefono works as Id
        if(selectedIds.contains(id))
            selectedIds.remove(id)
        else
            selectedIds.add(id)

        notifyItemChanged(index)
        if(selectedIds.size < 1) InicioFragment.isMultiseleccion = false
        inicioInterface.updateActionMode(selectedIds.size)
    }


    inner class inicioViewHolder(
        val recycler_item_invitados: ItemInvitadosBinding,
        val tap: ICardItemClickListener
    ) : RecyclerView.ViewHolder(recycler_item_invitados.root), View.OnClickListener, View.OnLongClickListener {

        //lateinit var listener: ICardItemClickListener
        val frameLayout: FrameLayout

        init {
            //itemView.setOnClickListener(this)
            frameLayout = itemView.findViewById(R.id.root_item_invitados)
            frameLayout.setOnClickListener(this)
            frameLayout.setOnLongClickListener(this)
        }
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

      /*  fun setEvent(listener: ICardItemClickListener){
            this.listener = listener
        }*/

        override fun onClick(v: View?) {
            if (v != null) {
                //listener.onItemClicked(v, adapterPosition)
                tap.onItemClicked(adapterPosition)
            }
        }

        override fun onLongClick(v: View?): Boolean {
           tap.onLongTap(adapterPosition)
            return true
        }


    }



}


