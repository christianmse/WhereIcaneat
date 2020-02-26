package com.whereicaneat.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.domain.data.Repositorio

@Suppress("UNCHECKED_CAST")
class LandingViewModelFactory(
    private val repository: Repositorio
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LandingViewModel(repository) as T
    }
}
