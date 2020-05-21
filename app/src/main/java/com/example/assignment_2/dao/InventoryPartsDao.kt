package com.example.assignment_2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assignment_2.model.InventoriesParts


@Dao
interface InventoryPartsDao {
    @Query("SELECT * FROM InventoriesParts")
    fun getAll(): List<InventoriesParts>

    @Query("SELECT * FROM InventoriesParts WHERE InventoryID = :inventoryID")
    fun getAllInventoryParts(inventoryID: Int): List<InventoriesParts>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(inventoriesParts: InventoriesParts)

    @Query("DELETE FROM InventoriesParts")
    fun removeAll()

}