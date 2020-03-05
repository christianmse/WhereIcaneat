package com.whereicaneat.ui.inicio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.mbms.MbmsErrors
import android.util.Log
import androidx.core.content.ContextCompat
import com.whereicaneat.R
import java.lang.Exception
import java.util.jar.Manifest

class InicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
    }
        /*val i = Intent()

        i.action = Intent.ACTION_OPEN_DOCUMENT
        i.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(i, 2)

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            try {
                Log.e("111111", "Todo bien")
            } catch (e: Exception){
                Log.e("1111111", e.toString())
            }
        }
    }*/
}
