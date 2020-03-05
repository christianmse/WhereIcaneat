package com.whereicaneat.ui.landing

import android.view.View
import com.whereicaneat.domain.data.db.entities.Restaurante

interface RecyclerViewClickListener {
    fun onRecyclerViewCartaClick(view: View, restaurante: Restaurante)

    fun setOnSelectedRestaurante(position: Int)


}