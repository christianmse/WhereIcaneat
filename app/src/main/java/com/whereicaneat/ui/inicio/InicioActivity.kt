package com.whereicaneat.ui.inicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whereicaneat.R

class InicioActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        var fragment = InicioFragment(restaurantesSelec)


        if(savedInstanceState == null){
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_Inicio, fragment as Fragment)
                    .commit()
        }
        setContentView(R.layout.activity_inicio)
    }
}
