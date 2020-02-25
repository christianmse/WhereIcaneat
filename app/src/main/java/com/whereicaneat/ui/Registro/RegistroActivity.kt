package com.whereicaneat.ui.Registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.remote.Repositorio
import com.whereicaneat.ui.Landing.LandingViewModel
import com.whereicaneat.ui.Landing.LandingViewModelFactory
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val database = DatabaseLocal(applicationContext)
        val repository = Repositorio(database)
        val factory = RegistroViewModelFactory(repository)
        val registroViewModel =
            ViewModelProviders.of(this, factory).get(RegistroViewModel::class.java)


    }




}
