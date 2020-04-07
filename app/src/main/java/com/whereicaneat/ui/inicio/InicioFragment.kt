package com.whereicaneat.ui.inicio

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.squareup.okhttp.ResponseBody
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.common.CurrentUser
import com.whereicaneat.common.EspacioItemInvitados
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Restaurante
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.domain.data.remote.ApiUtils
import com.whereicaneat.domain.data.remote.FCM
import com.whereicaneat.ui.push.PushActivity
import kotlinx.android.synthetic.main.inicio_fragment.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


class InicioFragment(
    var restaurantesSelec: Array<Parcelable>?
) : Fragment(){
    lateinit var i: Intent
    lateinit var mRequestQueue: RequestQueue
    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var factory: InicioViewModelFactory
    lateinit var usuarios: List<Usuario>
    private lateinit var inicioViewModel: InicioFragmentViewModel
    private lateinit var adapter: InicioAdapter
    lateinit var usuariosSelec: List<Usuario>


    val onClickedListener= object: InicioAdapter.OnClickedListener{
        override fun onItemClick(view: View?, obj: Usuario?, pos: Int) {
            adapter.toggleSelection(pos)
        }

        override fun onItemLongClick(view: View?, obj: Usuario?, pos: Int) {
            val usuarioAux: Usuario = adapter.getItem(pos)
            Toast.makeText(context, "Invita a: " + usuarioAux.nombreUsuario, Toast.LENGTH_SHORT)
                .show()

        }
    }

    companion object{
        var isMultiseleccion = false
    }

    init {
        Log.d("constructor","eds")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inicio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        database = DatabaseLocal(context!!)
        repository = Repositorio(database)
        factory = InicioViewModelFactory(repository)
        isMultiseleccion = false
        inicioViewModel =
            ViewModelProviders.of(this, factory).get(InicioFragmentViewModel::class.java)
        adapter = InicioAdapter(context!!)

        val layoutManager = GridLayoutManager(requireContext(), Common.NUM_OF_COLUMN)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (adapter != null){
                    when(adapter!!.getItemViewType(position)){
                        1 -> 1
                        0 -> Common.NUM_OF_COLUMN
                        else -> 1
                    }
                } else -1
            }

        }

        recyclerInicio.layoutManager = layoutManager
        recyclerInicio.addItemDecoration(EspacioItemInvitados(2))
        recyclerInicio.adapter = adapter

        shimmer_view_container.startShimmer()
        inicioViewModel.getUsuariosRemote()
        inicioViewModel.invitados.observe(viewLifecycleOwner, Observer { usuarios ->
            this.usuarios = usuarios
            shimmer_view_container.stopShimmer()
            shimmer_view_container.hideShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setListData(usuarios)
            adapter.notifyDataSetChanged()
        })

        adapter.putOnClickedListener(onClickedListener)

        btn_empezar.setOnClickListener {
            usuariosSelec =  adapter.getSelectedItems()!!
            inicioViewModel.sendUsuariosSelected(usuariosSelec)
            crearGrupoNotification(activity).execute(usuariosSelec)
             i = Intent(context, PushActivity::class.java)
            //Pasarle los restaurantes elegidos
            i.putExtra("restaurantesSelec", restaurantesSelec)
            i.putExtra("usuariosSelec", usuariosSelec.toTypedArray())
            activity?.startActivity(i)
        }

    }




    inner class crearGrupoNotification(private var activity: Activity?) : AsyncTask<List<Usuario>, Void, JSONObject>() {

        var listaTokens: MutableList<String>? = mutableListOf<String>()
        var listaRestaurantes: MutableList<String>? = mutableListOf<String>()
        var nombreRemitente = CurrentUser.nombre


        override fun onPreExecute() {
            super.onPreExecute()
            activity?.MyprogressBar?.visibility = View.VISIBLE
            mRequestQueue = Volley.newRequestQueue(activity)
        }

        override fun doInBackground(vararg params: List<Usuario>?): JSONObject {

            params[0]?.forEach {
                var token = (it as Usuario).token
                //if(CurrentUser.token != token) Impide que se lo envie a el mismo
                listaTokens?.add(token!!)
            }

            restaurantesSelec?.forEach {
                var restaurante = (it as Restaurante).nombre.toString()
                listaRestaurantes?.add(restaurante)
            }

            val url = URL("https://fcm.googleapis.com/fcm/send")
            val con = url.openConnection() as HttpsURLConnection
            con.doOutput = true

            // HTTP request header
            con.setRequestProperty("project_id", getString(R.string.SENDER_ID))
            con.setRequestProperty("Content-Type", "application/json")
            con.setRequestProperty("Accept", "application/json")
            con.setRequestProperty("Authorization", "key=${getString(R.string.API_KEY)}")
            con.requestMethod = "POST"


            // HTTP request
            val data = JSONObject()
            data.put("operation", "add")
            data.put("notification_key_name", "hola22")
            data.put("registration_ids", JSONArray(listaTokens))
            val notification = JSONObject()
            notification.put("title","Hora de comer")
            notification.put("body", "$nombreRemitente te ha invitado a votar el restaurante de hoy")
            data.put("notification",notification)
            val extraData = JSONObject()
            extraData.put("restaurantes", JSONArray(listaRestaurantes))
            data.put("data", extraData)

            val os = con.outputStream
            os.write(data.toString().toByteArray(charset("UTF-8")))
            con.connect()
            os.close()

            if(con.responseCode == HttpURLConnection.HTTP_OK){
                Log.d("crearGrupoNotification_ASYNC", "HTTP OK")
            }
            else{
                Log.e("crearGrupoNotification_ASYNC", "error request, error code:${con.responseCode}")
            }

            // Read the response into a string
            val `is` = con.inputStream
            val responseString = Scanner(`is`, "UTF-8").useDelimiter("\\A").next()
            `is`.close()

            // Parse the JSON string and return the notification key
            return JSONObject(responseString)

        }

        override fun onPostExecute(result: JSONObject?) {
            super.onPostExecute(result)
            activity?.MyprogressBar?.visibility = View.INVISIBLE
        }
    }
}





