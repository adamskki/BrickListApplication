package com.example.assignment_2.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import java.io.File


class MainActivity : AppCompatActivity() {

    private var database: dataBaseConnection? = null

    private val xmlMapper = XmlMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("BrickList");

        val fab: View = findViewById(R.id.addProducts)
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddProjectActivity::class.java)
            // start your next activity
            startActivity(intent)
        }


        xmlMapper.setDefaultUseWrapper(false)
        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

//        Observable.fromCallable{
//            database = dataBaseConnection.getInstance(this)
//
////            val test = Inventories(active = 1, lastAccessed = 2, name = "Halo" )
////            database!!.inventoryDao().insert(test)
//            println("Halo")
//
//        }
//        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()


        val httpAsync = "http://fcds.cs.put.poznan.pl/MyWeb/BL/615.xml".httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
//                        println(data)
                        val value: InventoryXML = xmlMapper.readValue(data, InventoryXML::class.java)

                        //path to com.example.bricklist
//                        val pathFile = filesDir.absolutePath + "/inventory.xml"

                        //writing
//                        xmlMapper.writeValue(File(pathFile), value)

//                        val test = xmlMapper.readValue(File(pathFile), InventoryXML::class.java)
//                        println(test)
                    }
                }
            }
        httpAsync.join()
    }

    override fun onResume() {
        super.onResume()

        var inventoryListProjects:List<Inventories>
        var adapter:InventoriesListAdapter
        val recycleInventories = findViewById<View>(R.id.inventoryRecycleView) as RecyclerView
        recycleInventories.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))


        Observable.fromCallable{
            database = dataBaseConnection.getInstance(this)

            inventoryListProjects = database!!.inventoryDao().getAll()
            runOnUiThread {
                adapter = InventoriesListAdapter(inventoryListProjects, this)
                recycleInventories.adapter = adapter
            }


        }
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()


        recycleInventories.layoutManager = LinearLayoutManager(this)


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
