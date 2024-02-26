package com.example.shayari.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shayari.adapter.FavouriteAdapter
import com.example.shayari.database.MyDatabaseHelper
import com.example.shayari.databinding.ActivityFavourietShayariBinding
import com.example.shayari.favouriteModal

class FavouriteShayariActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavourietShayariBinding
    var list = ArrayList<favouriteModal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavourietShayariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the view
        initview()
    }

    private fun initview() {
        // Using Kotlin's 'with' function to avoid repetitive 'binding' references
        with(binding) {

            // Initialize the database helper class object
            val db = MyDatabaseHelper(this@FavouriteShayariActivity, "ShayariDB.db")

            // Retrieve the list of favorite Shayari records from the database
            list = db.FavoriteDisplayRecord()

            // Initialize the FavouriteAdapter with the retrieved list and a click listener for updating the database
            var favorite = FavouriteAdapter(this@FavouriteShayariActivity, list) { shayariId, status ->
                db.UpdeteRecord(shayariId, status)
            }

            // Set up the RecyclerView with a LinearLayoutManager
            var manager = LinearLayoutManager(this@FavouriteShayariActivity, LinearLayoutManager.VERTICAL, false)
            rvcFavouriteShayari.layoutManager = manager
            rvcFavouriteShayari.adapter = favorite

            // Update the adapter's list
            favorite.UpdateList(list)
        }
    }
}
