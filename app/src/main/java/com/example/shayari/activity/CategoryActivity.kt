package com.example.shayari.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.adapter.CategoryAdapter
import com.example.shayari.database.MyDatabaseHelper
import com.example.shayari.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var myDb: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the view
        initview()
    }

    private fun initview() {
        // Using Kotlin's 'with' function to avoid repetitive 'binding' references
        with(binding) {

            // Click listener for navigating to the FavouriteShayariActivity
            llFavouriet.setOnClickListener {
                val intent = Intent(this@CategoryActivity, FavouriteShayariActivity::class.java)
                startActivity(intent)
            }

            // Initializing the database helper and importing the database from assets
            myDb = MyDatabaseHelper(this@CategoryActivity, "ShayariDB.db")
            myDb.importDataBaseFromAssets()

            // Reading the list of categories from the database
            val list = myDb.readCategory()

            // Creating an adapter for the RecyclerView
            val adapter = CategoryAdapter(list, displayCategory = { categoryName ->
                // Click listener for navigating to the DisplayShayariActivity with the selected category
                Log.d("TAG", "initview==>: $categoryName")
                val intent = Intent(this@CategoryActivity, DisplayShayariActivity::class.java)
                intent.putExtra("CategoryName", categoryName)
                startActivity(intent)
            })

            // Setting up the RecyclerView with a GridLayoutManager
            val manager = GridLayoutManager(this@CategoryActivity, 2, LinearLayoutManager.VERTICAL, false)
            rcvCategory.layoutManager = manager
            rcvCategory.adapter = adapter
        }
    }
}
