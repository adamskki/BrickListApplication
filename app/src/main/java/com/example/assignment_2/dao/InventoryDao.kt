package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignment_2.model.Inventories

@Dao
interface InventoryDao {
    @Query("SELECT * FROM Inventories")
    fun getAll(): List<Inventories>

    @Query("DELETE FROM Inventories")
    fun removeAll()

    @Query("SELECT id FROM Inventories WHERE name= :name")
    fun getID(name: String):List<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(inventories: Inventories)
}