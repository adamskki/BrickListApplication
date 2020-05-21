package com.example.assignment_2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_2.R
import com.example.assignment_2.project.ItemModel
import kotlinx.android.synthetic.main.item.view.*

class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
//    val itemImage: ImageView = view.itemImage
    val itemName: TextView = view.itemName
    val itemCode: TextView = view.itemCode
    val itemAmount: TextView = view.itemAmount
}

class ItemListAdapter(private val items: ArrayList<ItemModel>, private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Glide.with(context)
//            .load("https://fancycrave.com/wp-content/uploads/2019/12/Free-Nature-Pictures-min.jpg")
//            .into(holder.itemImage)
        holder.itemName.text = items[position].name
        holder.itemCode.text = items[position].color
        holder.itemAmount.text = items[position].amount
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }
}