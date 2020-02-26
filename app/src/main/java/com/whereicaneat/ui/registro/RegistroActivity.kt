package com.whereicaneat.ui.registro

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.databinding.ActivityRegistroBinding
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.landing.LandingActivity
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_registro.*
import java.io.IOException

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

        btn_imagen_registro.setOnClickListener {
            onGaleriaBotonClicked()
        }
       

    }


    override fun onStart() {
        super.onStart()
        val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInAnonymously().addOnSuccessListener {
            Log.e("signin_registro", "sign in bien hecho")
        }.addOnFailureListener {
            Log.e("signin_registro_fallo", it.toString())
        }
    }



    fun onGaleriaBotonClicked(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),1)
    }

    /*fun observarData(viewModel: RegistroViewModel){
        viewModel.dondeEstaElUsuario().observe(this, Observer {
            titulo_registro.text = it
        })
    }*/

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val database = DatabaseLocal(applicationContext)
        val repository = Repositorio(database)
        val factory =
            RegistroViewModelFactory(repository)
        val registroViewModel =
            ViewModelProviders.of(this, factory).get(RegistroViewModel::class.java)

        if(resultCode == Activity.RESULT_OK){
            try{
                val uri = data!!.data
                upload_imagen.setImageURI(uri)
                registroViewModel.uriImagen = uri.toString()
            } catch(e: IOException){
                e.printStackTrace()
            }
        }
    }
    override fun onSuccess() {
        tostada("Usuario registrado")
    }

    override fun onFailed(mensaje: String) {
        tostada(mensaje)
    }


}
