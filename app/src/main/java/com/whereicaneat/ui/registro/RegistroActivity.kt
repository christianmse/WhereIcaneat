package com.whereicaneat.ui.registro

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
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
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.net.CacheResponse

class RegistroActivity : AppCompatActivity(), RegistroListener, KodeinAware {

    override val kodein by kodein()
    private val factory: RegistroViewModelFactory by  instance()
    private lateinit var registroViewModel: RegistroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistroBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_registro)

        registroViewModel =
            ViewModelProviders.of(this, factory).get(RegistroViewModel::class.java)

        binding.registroViewModel = registroViewModel
        registroViewModel.regListener = this

        btn_imagen_registro.setOnClickListener {
            onGaleriaBotonClicked()
        }


    }

    override fun onStart() {
        super.onStart()


    }



    fun onGaleriaBotonClicked(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),1)
    }



    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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

    override fun onPause() {
        super.onPause()
    }
    override fun onSuccess(loginResponse: LiveData<Boolean>) {
        loginResponse.observe(this, Observer {
            if(it){
                tostada("Usuario registrado")
                startActivity(Intent(this, LandingActivity::class.java))
            }


        })

    }

    override fun onFailed(mensaje: String) {
        tostada(mensaje)
    }




}
