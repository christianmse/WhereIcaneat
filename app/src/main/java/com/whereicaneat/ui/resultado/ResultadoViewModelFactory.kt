package com.whereicaneat.ui.resultado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.votacion.VotadoViewModel

class ResultadoViewModelFactory(
    private val repository: Repositorio
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ResultadoViewModel(repository) as T
    }
}