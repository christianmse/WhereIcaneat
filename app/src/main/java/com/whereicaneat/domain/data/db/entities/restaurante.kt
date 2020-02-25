package com.whereicaneat.domain.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurante")
class restaurante(
    @ColumnInfo(name = "nombre")
    var nombre: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}