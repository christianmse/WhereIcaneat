package com.whereicaneat

import android.app.Application
import com.whereicaneat.data.db.entities.DatabaseLocal
import com.whereicaneat.domain.data.Repositorio
import com.whereicaneat.ui.inicio.InicioViewModelFactory
import com.whereicaneat.ui.landing.LandingViewModelFactory
import com.whereicaneat.ui.registro.RegistroViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
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


    }

}