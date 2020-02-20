package com.whereicaneat.ui.Registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.whereicaneat.data.repository.Repositorio

@Suppress("UNCHECKED_CAST")
class RegistroViewModelFactory(
    private val repository: Repositorio
):ViewModelProvider.NewInstanceFactory() {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return RegistroViewModel(repository) as T
    }
}