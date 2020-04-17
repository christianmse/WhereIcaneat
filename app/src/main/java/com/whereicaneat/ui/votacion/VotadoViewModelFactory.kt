package com.whereicaneat.ui.votacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.landing.LandingViewModel

class VotadoViewModelFactory(
    private val repository: Repositorio
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VotadoViewModel(repository) as T
    }
}