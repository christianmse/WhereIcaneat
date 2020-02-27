package com.whereicaneat.ui.inicio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.usuario

class InicioFragmentViewModel(
    private val repository: Repositorio
) : ViewModel() {
    private val invitados = MutableLiveData<List<usuario>>()
    
}
