package com.whereicaneat.ui.push

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.domain.data.Repositorio

class PushViewModelFactory(private val repository: Repositorio
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PushViewModel(repository) as T
    }
}
