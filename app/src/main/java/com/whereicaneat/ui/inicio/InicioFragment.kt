package com.whereicaneat.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.common.EspacioItemInvitados
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.domain.data.db.entities.Usuario
import com.whereicaneat.ui.push.PushActivity
import kotlinx.android.synthetic.main.inicio_fragment.*

class InicioFragment : Fragment(){

    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var factory: InicioViewModelFactory
    lateinit var usuarios: List<Usuario>
    private lateinit var inicioViewModel: InicioFragmentViewModel
    private lateinit var adapter: InicioAdapter

    val onClickedListener= object: InicioAdapter.OnClickedListener{
        override fun onItemClick(view: View?, obj: Usuario?, pos: Int) {
            adapter.toggleSelection(pos)
        }

        override fun onItemLongClick(view: View?, obj: Usuario?, pos: Int) {
            val usuarioAux: Usuario = adapter.getItem(pos)
            Toast.makeText(context, "Read: " + usuarioAux.nombreUsuario, Toast.LENGTH_SHORT)
                .show()

        }
    }

    companion object{
        var isMultiseleccion = false
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
            Log.e("1111111111111", adapter.getSelectedItems().toString())
            val i = Intent(context, PushActivity::class.java)
            //Pasarle los restaurantes elegidos
            i.putExtra("restaurantes", "macas")
            activity?.startActivity(i)
        }
    }








    /*inner class ActionModeCallback : ActionMode.Callback {
        var shouldResetRecyclerView = true

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
           *//* when (item?.getItemId()) {
                R.id.action_delete -> {
                    shouldResetRecyclerView = false
                    myAdapter?.deleteSelectedIds()
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish()
                    return true
                }
            }*//*
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.getMenuInflater()
            inflater?.inflate(R.menu.tap_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.findItem(R.id.action_llamar)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            if (shouldResetRecyclerView) {
                adapter?.selectedIds?.clear()
                adapter?.notifyDataSetChanged()
            }
            isMultiseleccion = false
            actionMode = null
            shouldResetRecyclerView = true
        }
        }*/


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

