package com.whereicaneat.domain.data.db.entities

import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
class usuario (
    @Nullable
    @ColumnInfo(name = "ImageUri")
    val imageUrl: Uri? = null,
    @ColumnInfo(name = "nombreUsuario")
    var nombreUsuario: String = "DEFAULT NOMBRE",
    @PrimaryKey
    @NonNull
    var telefono: String
){

}