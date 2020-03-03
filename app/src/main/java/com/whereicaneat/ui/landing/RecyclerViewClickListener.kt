package com.whereicaneat.ui.landing

import android.view.View
import com.whereicaneat.domain.data.db.entities.Restaurante

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, restaurante: Restaurante)


}