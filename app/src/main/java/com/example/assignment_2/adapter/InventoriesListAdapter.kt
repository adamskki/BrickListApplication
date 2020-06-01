package com.example.assignment_2.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.activites.ProjectActivity
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.model.Inventories
import com.example.assignment_2.project.ItemModel
import com.google.android.material.switchmaterial.SwitchMaterial
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.inventories.view.*
import kotlinx.android.synthetic.main.item.view.*


class InventoryViewHolder(view:View): RecyclerView.ViewHolder(view) {
    //    val itemImage: ImageView = view.itemImage
    val inventoryName: TextView = view.inventoryName
    val inventorySwitch: SwitchMaterial = view.archiveSwitch
    var inventoryID: Int = 0

}

class InventoriesListAdapter(private val inventoryList: List<Inventories>, private val context: Context) :
    RecyclerView.Adapter<InventoryViewHolder>() {
    private var database: dataBaseConnection? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.inventories, parent, false)
        )
    }


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holderInventory: InventoryViewHolder, position: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        val showArchived = sharedPreferences.getBoolean("Archive", true)
        holderInventory.inventoryName.text = inventoryList[position].name
        holderInventory.inventoryID = inventoryList[position].id
        holderInventory.inventoryName.setOnClickListener {
            val intentProject = Intent(context, ProjectActivity::class.java)
            intentProject.putExtra("id", holderInventory.inventoryID)
            context.startActivity(intentProject)
        }
        var active:Int? = null

        Observable.fromCallable{
            database = dataBaseConnection.getInstance(context)
            active = database!!.inventoryDao().getActive(inventoryList[position].id)

        }
            .doOnNext{
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                run {
                    holderInventory.inventorySwitch.isChecked = active == 0
                }
            }
        if(!showArchived && active == 0) {
            val params: ViewGroup.LayoutParams = holderInventory.itemView.layoutParams
            holderInventory.itemView.visibility = View.GONE
            params.height = 0
            params.width = 0
            holderInventory.itemView.layoutParams = params
        }


        holderInventory.inventorySwitch?.setOnCheckedChangeListener({ _ , isChecked ->
            if (isChecked) {
                Observable.fromCallable{
                    database = dataBaseConnection.getInstance(context)

                }
                    .doOnNext{
                        database!!.inventoryDao().setNonActivated(inventoryList[position].id)
                    }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                if(!showArchived) {
                    val params: ViewGroup.LayoutParams = holderInventory.itemView.layoutParams
                    holderInventory.itemView.visibility = View.GONE
                    params.height = 0
                    params.width = 0
                    holderInventory.itemView.layoutParams = params
                }
            }
            else {
                Observable.fromCallable{
                    database = dataBaseConnection.getInstance(context)

                }
                    .doOnNext{
                        database!!.inventoryDao().setActivated(inventoryList[position].id)
                    }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
        })

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return inventoryList.size
    }
}