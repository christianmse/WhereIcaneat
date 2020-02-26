package com.whereicaneat.util

import android.content.Context
import android.widget.Toast

fun Context.tostada(mensaje:String){
    Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show()
}