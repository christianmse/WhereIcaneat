package com.whereicaneat.ui.inicio

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.inicio_fragment.*
import java.lang.Exception

class InicioFragment : Fragment() {

    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var factory: InicioViewModelFactory
    private lateinit var inicioViewModel : InicioFragmentViewModel
    private lateinit var adapter:InicioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inicio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        database = DatabaseLocal (context!!)
        repository = Repositorio(database)
        factory = InicioViewModelFactory(repository)

        inicioViewModel = ViewModelProviders.of(this, factory).get(InicioFragmentViewModel::class.java)
        adapter = InicioAdapter(context!!)
        recyclerInicio.layoutManager =  LinearLayoutManager(requireContext())
        recyclerInicio.adapter = adapter

            inicioViewModel.getUsuariosRemote()
            inicioViewModel.invitados.observe(viewLifecycleOwner, Observer {usuarios ->
                adapter.setListData(usuarios)
                adapter.notifyDataSetChanged()
            })



    }


    /*fun observarData(viewModel: InicioFragmentViewModel){
        shimmer2_view_container.startShimmer()
        viewModel.getUsuariosRemote().observe(viewLifecycleOwner, Observer {
            shimmer2_view_container.stopShimmer()
            shimmer2_view_container.hideShimmer()
            shimmer2_view_container.visibility = View.GONE
            //adapter
        })
    }*/


  /*  fun tienePermisos():Boolean {
        var resul = false

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            context?.tostada("no tiene permisos")

            val flags: Int = (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                flags)
            resul =true
        } else {
            resul = true
        }

        if (Build.VERSION.SDK_INT < 19) {
            val i = Intent()
            i.action = Intent.ACTION_OPEN_DOCUMENT
            i.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(i, 2)
        }

        return resul
    }*/

  /*  @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            try {
                Log.e("111111", "odo bien")
            } catch (e:Exception){
                Log.e("1111111", e.toString())
            }
        }
    }*/
}

