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

    @Query("SELECT * FROM Inventories WHERE Active= 1")
    fun getAllActive(): List<Inventories>

    @Query("UPDATE Inventories SET Active =0 WHERE id =:itemID")
    fun setNonActivated(itemID: Int)

    @Query("UPDATE Inventories SET Active =1 WHERE id =:itemID")
    fun setActivated(itemID: Int)

    @Query("SELECT Active FROM Inventories WHERE id =:itemID")
    fun getActive(itemID: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(inventories: Inventories)
}