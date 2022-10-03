package com.example.crudapp

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    private var dataList: ArrayList<CustomerModel> = ArrayList()
    private var onClickItem: ((CustomerModel) -> Unit)? = null
    private var onClickDeleteItem: ((CustomerModel) -> Unit)? = null

    fun addItems(items: ArrayList<CustomerModel>) {
        this.dataList = items
        notifyDataSetChanged()
    }

    fun setOnClickItem(callback: (CustomerModel) -> Unit) {
        this.onClickItem = callback
    }
    fun setOnClickDeleteItem(callback: (CustomerModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items, parent, false)
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindView(data)
        holder.itemView.setOnClickListener { onClickItem?.invoke(data)}
        holder.btnDelete.setOnClickListener {onClickDeleteItem?.invoke(data) }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class DataViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.idView)
        private var name = view.findViewById<TextView>(R.id.nameView)
        private var age = view.findViewById<TextView>(R.id.ageView)
        private var isActive = view.findViewById<TextView>(R.id.isActiveView)
        var btnDelete = view.findViewById<Button>(R.id.deleteButton)

        fun bindView(data: CustomerModel) {
            id.text = data.id.toString()
            name.text = data.name
            age.text = data.age.toString()
            isActive.text = data.isActive.toString()
        }
    }


}