package com.example.assignment_2.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.adapter.ItemListAdapter
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.model.InventoriesParts
import com.example.assignment_2.project.ItemModel
import com.example.assignment_2.project.XmlSaveParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class ProjectActivity : AppCompatActivity() {

    var brickList = ArrayList<ItemModel>()
    private var database: dataBaseConnection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        setTitle("Project")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id:Int = intent.getIntExtra("id",0)


        val rvContacts = findViewById<View>(R.id.itemListView) as RecyclerView
        rvContacts.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = ItemListAdapter(brickList,this)



        Observable.fromCallable {

            database = dataBaseConnection.getInstance(this)
        }.doOnNext {
            val inventoryPartsList = database!!.inventoryPartsDao().getAllInventoryParts(id)
            println(inventoryPartsList)
            for (brick in inventoryPartsList!!) {
                var item = ItemModel()
                val name = database!!.partDao().getName(brick.itemID)
                val color = database!!.colorDao().getColorName(brick.colorID.toString())
                val type = database!!.itemTypeDao().getCode(brick.typeID)
                if(brick.typeID == 0) continue
                item.name = name
                item.color = color
                item.amount = brick.quantityInStore
                item.maxAmount = brick.quantityInSet
                item.code = brick.itemID
                item.colorID = brick.colorID.toString()
                item.typeID = brick.typeID
                item.type = type
                brickList.add(item)
            }
            runOnUiThread {
                rvContacts.adapter = ItemListAdapter(brickList, this)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            val id:Int = intent.getIntExtra("id",0)
            val path = "${filesDir.absolutePath}/inventory${id}.xml"
            val xml = XmlSaveParser()
            xml.saveXML(brickList, File(path))

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}
