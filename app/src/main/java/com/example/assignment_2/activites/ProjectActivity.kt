package com.example.assignment_2.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.adapter.ItemListAdapter
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.project.ItemModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProjectActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var brickList = ArrayList<ItemModel>()
    private var database: dataBaseConnection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        setTitle("Project")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id:Int = intent.getIntExtra("id",0)


        val rvContacts = findViewById<View>(R.id.itemListView) as RecyclerView
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = ItemListAdapter(brickList,this)



        Observable.fromCallable {

            database = dataBaseConnection.getInstance(this)
        }.doOnNext {
            val inventoryPartsList = database!!.inventoryPartsDao().getAllInventoryParts(id)
            println(inventoryPartsList)
            for (brick in inventoryPartsList) {
                var item = ItemModel()
                val name = database!!.partDao().getName(brick.itemID)
                val color = database!!.colorDao().getColorName(brick.colorID.toString())
                val amount = "${brick.quantityInStore} of ${brick.quantityInSet}"
                item.name = name
                item.color = color
                item.amount = amount
                item.code = brick.itemID
                item.colorID = brick.colorID.toString()
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

}
