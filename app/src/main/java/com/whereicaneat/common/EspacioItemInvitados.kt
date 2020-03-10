package com.whereicaneat.common

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EspacioItemInvitados(internal var espacio: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = espacio
        outRect.left = espacio
        outRect.right = espacio
        outRect.bottom = espacio
    }
}