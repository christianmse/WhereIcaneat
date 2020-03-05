package com.whereicaneat.domain.data.db.entities

import android.net.Uri
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.storage.FirebaseStorage

@Entity(tableName = "usuario")
class Usuario (
    @Nullable
    @ColumnInfo(name = "ImageUri")
    var imageUri: String = "DEFAULT URI",
    @ColumnInfo(name = "nombreUsuario")
    var nombreUsuario: String = "DEFAULT NOMBRE",
    @PrimaryKey
    @NonNull
    var telefono: String = "DEFAULT PHONE"
){

}