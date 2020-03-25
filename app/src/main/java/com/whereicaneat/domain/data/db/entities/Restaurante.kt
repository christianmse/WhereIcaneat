package com.whereicaneat.domain.data.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurante")
class Restaurante(
    @ColumnInfo(name = "ImageUri")
    val imageUri: String? = null,
    @ColumnInfo(name = "nombre")
    var nombre: String?,
    @ColumnInfo(name = "website")
    var website: String? = null
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUri)
        parcel.writeString(nombre)
        parcel.writeString(website)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurante> {
        override fun createFromParcel(parcel: Parcel): Restaurante {
            return Restaurante(parcel)
        }

        override fun newArray(size: Int): Array<Restaurante?> {
            return arrayOfNulls(size)
        }
    }
}