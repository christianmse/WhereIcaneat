package com.whereicaneat.ui.registro

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.databinding.ActivityRegistroBinding
import com.whereicaneat.ui.landing.LandingActivity
import com.whereicaneat.ui.push.ReceivedNotification
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.lang.Exception


class RegistroActivity : AppCompatActivity(), RegistroListener, KodeinAware {

    override val kodein by kodein()
    private val factory: RegistroViewModelFactory by  instance()
    private lateinit var registroViewModel: RegistroViewModel
    private lateinit var usuarioReg: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityRegistroBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_registro)

        registroViewModel =
            ViewModelProviders.of(this, factory).get(RegistroViewModel::class.java)

        binding.registroViewModel = registroViewModel
        registroViewModel.regListener = this
        usuarioReg = getSharedPreferences(Common.PREF_NAME, Common.PRIVATE_MODE)

        //Al hacer click a la push entra por aqui
        //usuarioReg.getBoolean("registrado", false)
        registroViewModel.login()

        if(usuarioReg.getBoolean("registrado", false)){
            val i = Intent(this,LandingActivity::class.java)
            if(intent.extras != null) {
                val restaurantes = intent.getStringExtra("restaurantes")
                val remitente = intent.getStringExtra("remitente")
                Log.e("intent_en_registerActivity", restaurantes)
                i.putExtra("restaurantes", restaurantes)
                i.putExtra("remitente", remitente)
            }
            startActivity(i)
        }

    }

    override fun onStart() {
        super.onStart()
        btn_imagen_registro.setOnClickListener {
            onGaleriaBotonClicked()
        }
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
