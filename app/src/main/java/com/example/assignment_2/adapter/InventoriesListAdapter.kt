package com.example.assignment_2.adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.activites.ProjectActivity
import com.example.assignment_2.model.Inventories
import com.example.assignment_2.project.ItemModel
import kotlinx.android.synthetic.main.inventories.view.*
import kotlinx.android.synthetic.main.item.view.*


class InventoryViewHolder(view:View): RecyclerView.ViewHolder(view) {
    //    val itemImage: ImageView = view.itemImage
    val inventoryName: TextView = view.inventoryName
    var inventoryID: Int = 0

}

class InventoriesListAdapter(private val inventoryList: List<Inventories>, private val context: Context) :
    RecyclerView.Adapter<InventoryViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.inventories, parent, false)
        )
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holderInventory: InventoryViewHolder, position: Int) {
        holderInventory.inventoryName.text = inventoryList[position].name
        holderInventory.inventoryID = inventoryList[position].id
        holderInventory.inventoryName.setOnClickListener {
//            Log.i("POZYCJA:",position.toString())
//            Log.i("ID:",holderInventory.inventoryID.toString())
            val intentProject = Intent(context, ProjectActivity::class.java)
            intentProject.putExtra("id", holderInventory.inventoryID)
            context.startActivity(intentProject)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return inventoryList.size
    }
}