package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.assignment_2.model.Codes


@Dao
interface CodeDao {
    @Query("SELECT * FROM Codes")
    fun getAll(): List<Codes>
}