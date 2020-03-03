package com.whereicaneat.ui.inicio

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.whereicaneat.R
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.inicio_fragment.*

class InicioFragment : Fragment() {

    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var factory: InicioViewModelFactory
    private lateinit var inicioViewModel : InicioFragmentViewModel


    private lateinit var viewModel: InicioFragmentViewModel

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
        inicioViewModel.getUsuariosRemote()
        inicioViewModel.invitados.observe(viewLifecycleOwner, Observer {usuarios ->
            recyclerInicio.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = InicioAdapter(usuarios, context!!)
            }
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

}

