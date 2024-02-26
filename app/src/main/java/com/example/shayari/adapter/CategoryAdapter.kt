package com.example.shayari.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R
import com.example.shayari.categoryModal

// Adapter for displaying categories in a RecyclerView
class CategoryAdapter(
    var list: ArrayList<categoryModal>,
    var displayCategory: ((CategoryName: String) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    // ViewHolder class holds references to the views in each list item
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtCategory: TextView = view.findViewById(R.id.txtCategory)
        var llLayout: LinearLayout = view.findViewById(R.id.llLayout)
        var imgCategoryImage: ImageView = view.findViewById(R.id.imgCategoryImage)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.MyViewHolder {
        // Inflate the layout for each category item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_file, parent, false)

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CategoryAdapter.MyViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view with that element
        with(holder) {
            // Set category name
            txtCategory.text = list[position].CategoryName

            // Set a click listener to handle category item clicks
            llLayout.setOnClickListener {
                // Invoke the displayCategory function with the clicked category name
                displayCategory.invoke(list[position].CategoryName)
            }

            // Decode Base64 image string and set the image
            val byteArray = Base64.decode(list[position].CategoryImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            imgCategoryImage.setImageBitmap(bitmap)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return list.size
    }
}
