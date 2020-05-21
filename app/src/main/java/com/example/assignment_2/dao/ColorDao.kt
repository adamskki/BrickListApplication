package com.example.assignment_2.dao


import androidx.room.Dao
import androidx.room.Query
import com.example.assignment_2.model.Colors

@Dao
interface ColorDao {
    @Query("SELECT * FROM Colors")
    fun getAll(): List<Colors>
    @Query("SELECT Name FROM Colors WHERE Code= :idColor")
    fun getColorName(idColor: String):String
}