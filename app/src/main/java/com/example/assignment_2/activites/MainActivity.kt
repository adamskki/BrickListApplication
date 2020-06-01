package com.example.assignment_2.activites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.adapter.InventoriesListAdapter
import com.example.assignment_2.adapter.ItemListAdapter
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.model.Inventories
import com.example.assignment_2.project.ItemModel
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var database: dataBaseConnection? = null
    private var inventoryListProjects: List<Inventories> = ArrayList()
    private var sharedPreferences: SharedPreferences? = null


    private val xmlMapper = XmlMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("BrickList");


        inventoryRecycleView.layoutManager = LinearLayoutManager(this)
        inventoryRecycleView.adapter = InventoriesListAdapter(inventoryListProjects, this)

        val fab: View = findViewById(R.id.addProducts)
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddProjectActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        val recycleInventories = findViewById<View>(R.id.inventoryRecycleView) as RecyclerView

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


        recycleInventories.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recycleInventories.layoutManager = LinearLayoutManager(this)
        val showArchived = sharedPreferences!!.getBoolean("Archive", true)


        Observable.fromCallable{
            database = dataBaseConnection.getInstance(this)
            inventoryListProjects = database!!.inventoryDao().getAll()
            if(!showArchived) {
                inventoryListProjects = inventoryListProjects.filter { inventory -> inventory.active == 1 }
            }
                runOnUiThread {
                    recycleInventories.adapter = InventoriesListAdapter(inventoryListProjects, this)
                }
        }
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
