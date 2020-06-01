package com.example.assignment_2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment_2.R
import com.example.assignment_2.database.dataBaseConnection
import com.example.assignment_2.project.ItemModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_project.view.*
import kotlinx.android.synthetic.main.item.view.*

class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
    val itemImage: ImageView = view.itemImage
    val itemName: TextView = view.itemName
    val itemCode: TextView = view.itemCode
    val itemAmount: TextView = view.itemAmount
    val itemCell: LinearLayout = view.itemCell
    val addBtn: Button = view.plusBtn
    val minBtn: Button = view.minusBtn
}

class ItemListAdapter(private val items: ArrayList<ItemModel>, private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    private var database: dataBaseConnection? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
        Glide.with(context)
            .load("https://www.lego.com/service/bricks/5/2/${items[position].code}").error(
                Glide.with(context)
                    .load("http://img.bricklink.com/P/${items[position].colorID}/${items[position].code}.gif")
                    .error(Glide.with(context).load("https://www.bricklink.com/PL/${items[position].code}.jpg"))
            )
            .into(holder.itemImage)
        holder.itemName.text = items[position].name
        holder.itemCode.text = items[position].color
        holder.itemAmount.text = "${items[position].amount} of ${items[position].maxAmount}"

        if(items[position].amount == items[position].maxAmount) {
            holder.itemCell.setBackgroundColor(Color.rgb(0, 255, 153))
        }
        if(items[position].amount != items[position].maxAmount) {
            holder.itemCell.setBackgroundColor(Color.rgb(255, 255, 255))
        }


        holder.addBtn.setOnClickListener {
            if(items[position].amount != items[position].maxAmount){
                items[position].amount = items[position].amount!! + 1
                holder.itemAmount.text = "${items[position].amount} of ${items[position].maxAmount}"
            }



            Observable.fromCallable{
                database = dataBaseConnection.getInstance(context)

            }
                .doOnNext{
                    database!!.inventoryPartsDao().updateQuantity(items[position].amount!!, items[position].code!!, items[position].typeID!!, items[position].colorID!!.toInt())
            }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()

            if(items[position].amount == items[position].maxAmount) {
                holder.itemCell.setBackgroundColor(Color.rgb(0, 255, 153))
            }
            if(items[position].amount != items[position].maxAmount) {
                holder.itemCell.setBackgroundColor(Color.rgb(255, 255, 255))
            }
        }
        holder.minBtn.setOnClickListener {
            if(items[position].amount != 0) {
                items[position].amount = items[position].amount!! - 1
                holder.itemAmount.text = "${items[position].amount} of ${items[position].maxAmount}"
            }


            Observable.fromCallable{
                database = dataBaseConnection.getInstance(context)

            }
                .doOnNext{
                    database!!.inventoryPartsDao().updateQuantity(items[position].amount!!, items[position].code!!, items[position].typeID!!, items[position].colorID!!.toInt())
                }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            if(items[position].amount == items[position].maxAmount) {
                holder.itemCell.setBackgroundColor(Color.rgb(0, 255, 153))
            }
            if(items[position].amount != items[position].maxAmount) {
                holder.itemCell.setBackgroundColor(Color.rgb(255, 255, 255))
            }

        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return items.size
    }
}