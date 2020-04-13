package com.whereicaneat.ui.inicio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.size
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.databinding.ItemInvitadosBinding
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.item_invitados.view.*
import java.lang.Exception
import java.util.*


class InicioAdapter(
    private val context: Context
) : RecyclerView.Adapter<InicioAdapter.inicioViewHolder>(){

    var onClickedListener:OnClickedListener? = null
    var selected_items = SparseBooleanArray()
    var current_selected_idx = -1
    var usuarios: List<Usuario> = mutableListOf()


    fun setListData(data: List<Usuario>) {
        usuarios = data

    }

    fun putOnClickedListener(onClickedListener: OnClickedListener){
        this.onClickedListener = onClickedListener
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

    override fun getItemCount(): Int {
        if(usuarios.isNotEmpty())
            return usuarios.size

        return 0;
    }
    fun getSelectedItemCount() = selected_items.size

    override fun onBindViewHolder(holder: inicioViewHolder, position: Int) {
        val usuarioAux: Usuario = usuarios[position]
        holder.recycler_item_invitados.usuarioModel = usuarioAux
        holder.bindView(usuarioAux)
        holder.recycler_item_invitados.lytParent.isActivated = selected_items.get(position,false)
        holder.recycler_item_invitados.lytParent.setOnClickListener {
            onClickedListener?.onItemClick(it,usuarioAux,position)
        }
        holder.recycler_item_invitados.lytParent.setOnLongClickListener(OnLongClickListener { v ->
            if (onClickedListener == null) return@OnLongClickListener false
            onClickedListener?.onItemLongClick(v, usuarioAux, position)
            true
        })
        toggleCheckedIcon(holder, position)



        //Antiguo:
        /*holder.setEvent(object:ICardItemClickListener{
            override fun onItemClicked(vista: View, posicion: Int) {
                context.tostada("Invita a ${usuarios[posicion].nombreUsuario}")
            }

        })*/
    }



    private fun resetCurrentIndex() {
        current_selected_idx = -1
    }

    fun toggleCheckedIcon(
        holder: InicioAdapter.inicioViewHolder,
        position: Int
    ) {
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

    fun toggleSelection(pos: Int) {
        current_selected_idx = pos
        if (selected_items[pos, false]) {
            selected_items.delete(pos)
        } else {
            selected_items.put(pos, true)
        }
        notifyItemChanged(pos)
    }

    fun clearSelections() {
        selected_items.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): MutableList<Usuario> {
        val items: MutableList<Usuario> =
            ArrayList(selected_items.size())
        for (i in 0 until usuarios.size) {
            if(selected_items[i])
            items.add(usuarios[i])
        }
        return items
    }

    fun getItem(position: Int) = usuarios.get(position)


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









    inner class inicioViewHolder(
        val recycler_item_invitados: ItemInvitadosBinding
    ) : RecyclerView.ViewHolder(recycler_item_invitados.root){

        //lateinit var listener: ICardItemClickListener
        val frameLayout: FrameLayout


        init {
            //itemView.setOnClickListener(this)
            frameLayout = itemView.findViewById(R.id.root_item_invitados)

        }
        fun bindView(usuario: Usuario) {

            val storageReference = FirebaseStorage
                .getInstance()
                .getReference("Imagenes_Perfil")
                .child(usuario.uid!!)

            Log.e("FBStorage", usuario.uid)

                storageReference
                    .getBytes(1024*1024)
                    .addOnSuccessListener {
                        var bitmap: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        itemView.img_invitado.setImageBitmap(bitmap)
                        itemView.image_letter.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Log.e("FBStorageError", it.toString())
                    }




        }


    }


    interface OnClickedListener {
        fun onItemClick(view: View?, obj: Usuario?, pos: Int)
        fun onItemLongClick(view: View?, obj: Usuario?, pos: Int)
    }

}


