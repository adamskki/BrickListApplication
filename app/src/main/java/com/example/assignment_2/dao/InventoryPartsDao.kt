package com.example.assignment_2.dao

import androidx.room.*
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

    @Query("UPDATE InventoriesParts SET QuantityInStore =:quantity WHERE ItemID =:itemID AND TypeID =:typeID AND ColorID =:colorID")
    fun updateQuantity(quantity: Int, itemID: String, typeID: Int, colorID: Int)

}