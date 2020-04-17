package com.whereicaneat.ui.landing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class LandingViewModel(
    private val repository: Repositorio
) : ViewModel(){
    val mutableData = MutableLiveData<MutableList<Restaurante>>()

    //Obtiene restaurantes de firestore
    fun getRestaurantesData():LiveData<MutableList<Restaurante>>{
        repository.getRestaurantesRemote().observeForever {
            mutableData.value = it
        }
        if(mutableData.value != null)
            setRestaurantesLocal()

        return mutableData
    }

    fun setRestaurantesLocal() {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                for(restaurante in mutableData.value!!)
                repository.insertarRestauranteLocal(restaurante)
            }catch (e: Exception){
                Log.e("guardarRestaurantesLocal", e.toString())
            }
        }
    }
    fun login() {
        repository.userLogin()
    }

    fun setCurrentUser() {
        repository.setcurrentUser()
    }
}