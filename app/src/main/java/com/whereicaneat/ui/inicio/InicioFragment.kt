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
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.whereicaneat.R
import com.whereicaneat.common.Common
import com.whereicaneat.common.EspacioItemInvitados
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.util.tostada
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.inicio_fragment.*
import java.lang.Exception

class InicioFragment : Fragment(), InicioInterface{

    lateinit var database: DatabaseLocal
    lateinit var repository: Repositorio
    lateinit var factory: InicioViewModelFactory
    private lateinit var inicioViewModel: InicioFragmentViewModel
    private lateinit var adapter: InicioAdapter
    var actionMode: ActionMode? = null

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
        adapter = InicioAdapter(context!!, this)
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

        inicioViewModel.getUsuariosRemote()
        inicioViewModel.invitados.observe(viewLifecycleOwner, Observer { usuarios ->
            adapter.setListData(usuarios)
            adapter.notifyDataSetChanged()
        })


    }

    override fun updateActionMode(size: Int) {
        if(actionMode == null)  actionMode = activity?.startActionMode(ActionModeCallback())
        if(size > 0) actionMode?.setTitle("$size")
        else actionMode?.finish()
    }

    inner class ActionModeCallback : ActionMode.Callback {
        var shouldResetRecyclerView = true

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
           /* when (item?.getItemId()) {
                R.id.action_delete -> {
                    shouldResetRecyclerView = false
                    myAdapter?.deleteSelectedIds()
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish()
                    return true
                }
            }*/
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
        }

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


