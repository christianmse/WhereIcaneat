package com.whereicaneat.util

import android.widget.ImageView
import  androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//ImageBinding
//Añadir atributo app:cargarImage="@{usuarioModel.imageUri}" a item_invitados
@BindingAdapter("cargarImage")
fun cargarImagen(view: ImageView, url: String){
    Glide
        .with(view)
        .load(url)
        .into(view)

}