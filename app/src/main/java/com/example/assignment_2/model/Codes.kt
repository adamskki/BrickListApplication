package com.example.assignment_2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Codes(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "ItemID") val itemID: Int,
    @ColumnInfo(name = "ColorID") val colorID: Int?,
    @ColumnInfo(name = "Code") val code: Int?,
    @ColumnInfo(name = "Image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray?
)