package com.whereicaneat.ui.inicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whereicaneat.R
import kotlinx.android.synthetic.main.activity_inicio.*

class InicioActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        val fragmentUsuarios = InicioFragment(restaurantesSelec)
        val fragmentFavoritos = InicioFragment(restaurantesSelec)
        val adapter = InicioPagerAdapter(supportFragmentManager)

        adapter.addFragmentToPager(fragmentUsuarios, "Usuarios")
        adapter.addFragmentToPager(fragmentFavoritos, "Favoritos")

        inicio_pager.adapter = adapter
        inicio_tablayout.setupWithViewPager(inicio_pager)
        /*if(savedInstanceState == null){
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout_Inicio, fragment as Fragment)
                    .commit()
        }*/

    }


}
