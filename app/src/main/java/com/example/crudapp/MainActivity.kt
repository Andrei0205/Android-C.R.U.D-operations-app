package com.example.crudapp

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

}
    private lateinit var binding: ActivityMainBinding

    private lateinit var SQLliteHelper: DataBaseHelper
    private var adapter: DataAdapter? = null
    private var data: CustomerModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SQLliteHelper = DataBaseHelper(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.addButton.setOnClickListener { addTable() }
        binding.updateButton.setOnClickListener { updateTable() }
        binding.viewButton.setOnClickListener { viewElements() }

        adapter?.setOnClickItem {
            data = it
            binding.inputName.setText(it.name.toString())
            binding.inputAge.setText(it.age.toString())
            binding.isActiveSwitch.isChecked = it.isActive.toBoolean()
        }
        adapter?.setOnClickDeleteItem {
            deleteTable(it.id)
        }

    }

    private fun addTable() {

        try {
            val name = binding.inputName.text.toString()
            val age = binding.inputAge.text.toString().toInt()
            val isActive = binding.isActiveSwitch.isChecked.toString()
            val customer = CustomerModel(
                1,
                name,
                age,
                isActive
            )
            val status = SQLliteHelper.insertColumn(customer)
            if (status > -1) {
                Toast.makeText(this, "Column Added", Toast.LENGTH_SHORT).show()
                clearEditText()
                hideKeyboard()
                viewElements()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("error", "${e.printStackTrace()}")
        }

    }

    private fun updateTable() {

        if (binding.inputAge.text.isEmpty() || binding.inputName.text.isEmpty()) {
            Toast.makeText(this, "Select column to update", Toast.LENGTH_SHORT).show()
            return
        }
        val name = binding.inputName.text.toString()
        val age = binding.inputAge.text.toString().toInt()
        val isActive = binding.isActiveSwitch.isChecked().toString()

        if (name == data?.name && age == data?.age && isActive == data?.isActive) {
            Toast.makeText(this, "record not changed", Toast.LENGTH_SHORT).show()
            return
        }
        if (data == null) return

        val data = CustomerModel(data!!.id, name, age, isActive)
        val success = SQLliteHelper.updateData(data)
        if (success > -1) {
            clearEditText()
            hideKeyboard()
            viewElements()
        } else {
            Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTable(id: Int) {
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            SQLliteHelper.deleteColumnById(id)
            viewElements()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun viewElements() {
        val dataList = SQLliteHelper.getAllColumns()
        adapter?.addItems(dataList)

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DataAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun clearEditText() {
        binding.inputAge.setText("")
        binding.inputName.setText("")
        binding.isActiveSwitch.isChecked = false
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
