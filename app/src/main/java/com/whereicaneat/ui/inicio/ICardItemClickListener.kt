package com.whereicaneat.ui.inicio

import android.view.View

interface ICardItemClickListener {
    //contara los items seleccionados
    fun onItemClicked(posicion: Int)
    fun onLongTap(index: Int)
}