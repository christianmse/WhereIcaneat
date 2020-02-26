package com.whereicaneat.ui.registro

interface RegistroListener {
    fun onSuccess()
    fun onFailed(mensaje: String)
}