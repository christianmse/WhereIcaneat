package com.whereicaneat.ui.inicio

import android.view.View

interface ICardItemClickListener {
    fun onItemClicked(vista: View, posicion: Int)
}