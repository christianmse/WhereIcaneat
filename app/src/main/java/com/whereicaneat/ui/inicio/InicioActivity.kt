package com.whereicaneat.ui.inicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.whereicaneat.R
import java.lang.Exception

class InicioActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var fragment = InicioFragment()

        if(savedInstanceState == null){
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_Inicio, fragment as Fragment)
                    .commit()
        }
        setContentView(R.layout.activity_inicio)
    }
}
