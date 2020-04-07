package com.whereicaneat.common

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whereicaneat.domain.data.db.entities.Usuario

object CurrentUser {
     lateinit var telefono: String
    lateinit var nombre:String
lateinit var uid:String
    lateinit var token:String

}