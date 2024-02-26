package com.example.shayari.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.adapter.DisplayShayariAdapter
import com.example.shayari.database.MyDatabaseHelper
import com.example.shayari.databinding.ActivityDisplayShayariBinding
import com.example.shayari.shayariModal

class DisplayShayariActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayShayariBinding
    private lateinit var quotesList: ArrayList<shayariModal>
    private lateinit var quotesAdapter: DisplayShayariAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayShayariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the view
        initview()
    }

    private fun initview() {
        // Using Kotlin's 'with' function to avoid repetitive 'binding' references
        with(binding) {

            // Pass the data of the category from the intent
            var categoryName = intent.getStringExtra("CategoryName")
            txtDisplayShayari.text = categoryName

            // Initialize the database helper class object
            val db = MyDatabaseHelper(this@DisplayShayariActivity, "ShayariDB.db")

            // Retrieve the list of Shayari for the given category from the database
            var list = db.displayShayari(categoryName)

            // Import assets from the database if needed
            db.importDataBaseFromAssets()

            // Pass the context (this@DisplayShayariActivity) to the adapter
            val adapter = DisplayShayariAdapter(this@DisplayShayariActivity, list, showcase = { shayari ->
                // Click listener for showcasing the Shayari
                Log.d("TAG", "addSh: $shayari")
                val intent = Intent(this@DisplayShayariActivity, ShayariShowcaseActivity::class.java)
                intent.putExtra("Shayari", shayari)
                startActivity(intent)
            })

            // Setting up the RecyclerView with a LinearLayoutManager
            val manager = LinearLayoutManager(this@DisplayShayariActivity, LinearLayoutManager.VERTICAL, false)
            rvcDisplayShayari.layoutManager = manager
            rvcDisplayShayari.adapter = adapter
        }
    }
}
