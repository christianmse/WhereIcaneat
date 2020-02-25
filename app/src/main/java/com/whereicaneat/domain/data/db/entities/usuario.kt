package com.whereicaneat.domain.data.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
class usuario (
    @ColumnInfo(name = "ImageUrl")
    val imageUrl: String = "DEFAULT  URL",
    @ColumnInfo(name = "nombreUsuario")
    var nombreUsuario: String = "DEFAULT NOMBRE",
    @PrimaryKey
    @NonNull
    var telefono: String
){

}