package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.assignment_2.model.ItemTypes


@Dao
interface ItemTypeDao {
    @Query("SELECT * FROM ItemTypes")
    fun getAll(): List<ItemTypes>
}