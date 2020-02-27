package com.whereicaneat.ui.registro

import androidx.lifecycle.LiveData

interface RegistroListener {
    fun onSuccess(loginResponse: LiveData<Boolean>)
    fun onFailed(mensaje: String)
}