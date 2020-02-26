package com.whereicaneat.ui.registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.databinding.ActivityRegistroBinding
import com.whereicaneat.domain.data.remote.Repositorio
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity(), RegistroListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistroBinding = DataBindingUtil.setContentView(this, R.layout.activity_registro)

        val database = DatabaseLocal(applicationContext)
        val repository = Repositorio(database)
        val factory =
            RegistroViewModelFactory(repository)
        val registroViewModel =
            ViewModelProviders.of(this, factory).get(RegistroViewModel::class.java)

        binding.registroViewModel = registroViewModel
        registroViewModel.regListener = this


    }



    override fun onSuccess() {
        tostada("Usuario registrado")
    }

    override fun onFailed(mensaje: String) {
        tostada(mensaje)
    }


}
