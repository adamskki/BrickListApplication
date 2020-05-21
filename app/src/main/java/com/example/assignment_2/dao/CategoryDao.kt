package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.assignment_2.model.Categories


@Dao
interface CategoryDao {
    @Query("SELECT * FROM Categories")
    fun getAll(): List<Categories>
}