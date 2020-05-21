package com.example.assignment_2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment_2.dao.*
import com.example.assignment_2.model.*

@Database(
    entities = [
    Categories::class,
    Codes::class,
    Colors::class,
    Inventories::class,
    InventoriesParts::class,
    ItemTypes::class,
    Parts::class
    ],
    version = 1
)


abstract class dataBaseConnection: RoomDatabase(){
    abstract fun categoryDao(): CategoryDao
    abstract fun codeDao(): CodeDao
    abstract fun colorDao(): ColorDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun inventoryPartsDao(): InventoryPartsDao
    abstract fun itemTypeDao(): ItemTypeDao
    abstract fun partDao(): PartDao

    companion object {
        private var INSTANCE: dataBaseConnection? = null

        fun getInstance(context: Context): dataBaseConnection? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    dataBaseConnection::class.java,
                    "BrickList"
                )
                    .createFromAsset("BrickList.db")
                    .build()
            }
            return INSTANCE
        }
    }
}