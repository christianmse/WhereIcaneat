package com.whereicaneat.ui.push

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import nl.dionsegijn.konfetti.models.Vector
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import kotlinx.android.synthetic.main.activity_push.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class PushActivity : AppCompatActivity() {

    lateinit var restaurantesSelec: Array<Parcelable>
    lateinit var usuariosSelec: Array<Parcelable>
    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var viewModel:PushViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)
        database = DatabaseLocal(baseContext)
        repository = Repositorio(database)
        viewModel = PushViewModel(repository)

        restaurantesSelec = intent.getParcelableArrayExtra("restaurantesSelec")
        usuariosSelec = intent.getParcelableArrayExtra("usuariosSelec")

        viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(200.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)

        val restaurante = (restaurantesSelec[0] as Restaurante).nombre
        viewModel.getRestauranteCount(restaurante!!).observeForever {
            it.forEach {
                gente.text = it.nombreUsuario
            }
        }
    }





}
