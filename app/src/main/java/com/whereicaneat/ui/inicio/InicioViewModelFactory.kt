package com.whereicaneat.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.landing.LandingViewModel

@Suppress("UNCHECKED_CAST")
class InicioViewModelFactory(
    private val repository: Repositorio
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InicioFragmentViewModel(repository) as T
    }
}

