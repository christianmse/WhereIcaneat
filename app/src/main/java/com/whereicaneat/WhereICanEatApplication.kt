package com.whereicaneat

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.inicio.InicioViewModelFactory
import com.whereicaneat.ui.landing.LandingActivity
import com.whereicaneat.ui.landing.LandingViewModelFactory
import com.whereicaneat.ui.push.PushViewModelFactory
import com.whereicaneat.ui.registro.RegistroActivity
import com.whereicaneat.ui.registro.RegistroViewModelFactory
import com.whereicaneat.ui.votacion.VotadoViewModelFactory
import com.whereicaneat.util.tostada
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.singleton
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WhereICanEatApplication: Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@WhereICanEatApplication))
        bind() from singleton { DatabaseLocal(instance()) }
        bind() from singleton { Repositorio(instance()) }
        bind() from provider {   LandingViewModelFactory(instance()) }
        bind() from provider {   RegistroViewModelFactory(instance()) }
        bind() from provider {   InicioViewModelFactory(instance()) }
        bind() from provider {   PushViewModelFactory(instance()) }
        bind() from provider {   VotadoViewModelFactory(instance()) }
    }
    private val repositorio:Repositorio by  instance()

}