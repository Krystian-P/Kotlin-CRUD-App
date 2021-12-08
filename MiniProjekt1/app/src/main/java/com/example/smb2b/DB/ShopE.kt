package com.example.smb2b.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopinfo")
data class ShopE (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int = 0,
    @ColumnInfo(name = "Nazwa") val nazwa: String,
    @ColumnInfo(name = "Cena") val cena: String,
    @ColumnInfo(name = "Ilosc") val ilosc: String?
)