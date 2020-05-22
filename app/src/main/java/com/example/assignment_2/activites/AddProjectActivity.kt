package com.example.assignment_2.activites

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.assignment_2.R
import kotlinx.android.synthetic.main.activity_add_project.*
import androidx.preference.PreferenceManager
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.model.Inventories
import com.example.assignment_2.model.InventoriesParts
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddProjectActivity : AppCompatActivity() {


    private var database: dataBaseConnection? = null
    private val xmlMapper = XmlMapper()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
        setTitle("New Project")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

        projectName.setHint("Name")
        projectCode.setHint("Project Code")

        val checkBtn: View = findViewById(R.id.checkBtn)
        checkBtn.setOnClickListener { view ->

            Observable.fromCallable{
                val name:String = projectName.text.toString()
                database = dataBaseConnection.getInstance(this)

                val projectID:List<Int> = database!!.inventoryDao().getID(name)


                if(projectID.isEmpty()){
                    runOnUiThread {
                        addBtn.isClickable = true
                        addBtn.isEnabled = true
                    }

                }
            }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
        }

        xmlMapper.setDefaultUseWrapper(false)
        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        val addBtn: View = findViewById(R.id.addBtn)
        addBtn.setOnClickListener { view ->
            val code:String = projectCode.text.toString()
            val name:String = projectName.text.toString()
            projectCode.text.clear()
            projectName.text.clear()
            var xmlData: InventoryXML? = null
            val url = sharedPreferences.getString("url", "")
            val httpAsync = "${url}${code}.xml".httpGet()
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            println(ex)
                        }
                        is Result.Success -> {
                            val data = result.get()
//                            println(data)
                            xmlData = xmlMapper.readValue(data, InventoryXML::class.java)


                        }
                    }
                }
            httpAsync.join()

            Observable.fromCallable{
                database = dataBaseConnection.getInstance(this)

                val currentDateTime = LocalDateTime.now()
//                database!!.inventoryDao().removeAll()
//                database!!.inventoryPartsDao().removeAll()

//                database!!.inventoryDao().removeAll()

                val newProject = Inventories(name = name, active = 1,lastAccessed = (currentDateTime.format(DateTimeFormatter.BASIC_ISO_DATE)).toInt())
                database!!.inventoryDao().insert(newProject)
                val projectID:List<Int> = database!!.inventoryDao().getID(newProject.name)


                for (item in xmlData!!.ITEM){
                    val typeID = database!!.partDao().getTypeID(item.itemId)
                    val itemID = item.itemId
                    val quantityInSet = item.quantity
                    val colorID = item.color
                    val inventoryPart = InventoriesParts(inventoryID = projectID[0], typeID = typeID, itemID = itemID, quantityInSet = quantityInSet, colorID = colorID, extra = item.extra)
                    database!!.inventoryPartsDao().insert(inventoryPart)
                }
                    runOnUiThread {
                        addBtn.isClickable = false
                        addBtn.isEnabled = false
                    }

            }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()


        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
