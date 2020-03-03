package com.whereicaneat.domain.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurante")
class restaurante(
    @ColumnInfo(name = "ImageUri")
    val imageUri: String? = null,
    @ColumnInfo(name = "nombre")
    var nombre: String,
    @ColumnInfo(name = "website")
    var website: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}