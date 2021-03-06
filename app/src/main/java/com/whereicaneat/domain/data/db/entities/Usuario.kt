package com.whereicaneat.domain.data.db.entities

import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
    var imageUri: String? = "DEFAULT URI",
    @ColumnInfo(name = "nombreUsuario")
    var nombreUsuario: String? = "DEFAULT NOMBRE",
    @PrimaryKey
    @NonNull
    var telefono: String = "DEFAULT PHONE",
    @ColumnInfo(name = "token")
    var token: String? = "DEFAULT TOKEN",
    @ColumnInfo(name = "uid")
    var uid: String? = "DEFAULT UID"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUri)
        parcel.writeString(nombreUsuario)
        parcel.writeString(telefono)
        parcel.writeString(token)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuario> {
        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }
    }



}