package com.whereicaneat.data.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
class usuario (
    @ColumnInfo(name = "nombreUsuario")
    var nombreUsuario: String,
    @PrimaryKey
    @NonNull
    var telefono: Int
){

}