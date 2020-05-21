package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.assignment_2.model.Codes
import com.example.assignment_2.model.Parts


@Dao
interface PartDao {
    @Query("SELECT * FROM Parts")
    fun getAll(): List<Parts>

    @Query("SELECT TypeID FROM Parts WHERE Code = :code")
    fun getTypeID(code: String): Int

    @Query("SELECT Name FROM Parts WHERE Code = :code")
    fun getName(code: String): String

    @Query("SELECT id FROM Parts WHERE Code = :code")
    fun getID(code: String): Int

}