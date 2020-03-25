package com.whereicaneat.ui.landing

import android.view.View
import com.whereicaneat.domain.data.db.entities.Restaurante

interface RecyclerViewClickListener {
    fun onRecyclerViewCartaClick(view: View, restaurante: Restaurante)

    fun setOnSelectedRestaurante(view: View?, obj:Restaurante?, position: Int)
    fun onItemClick(it: View?, restaurante: Restaurante, position: Int)

}