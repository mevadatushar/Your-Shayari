package com.example.shayari.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R
import com.example.shayari.database.MyDatabaseHelper
import com.example.shayari.shayariModal

// Adapter for displaying Shayari in a RecyclerView
class DisplayShayariAdapter(
    var context: Context,
    var list: ArrayList<shayariModal>,
    var showcase: ((Shayari: String) -> Unit)
) : RecyclerView.Adapter<DisplayShayariAdapter.MyViwHolder>() {

    // Start with the first background (g1)
    private var currentBackgroundIndex = 1

    // Declare SharedPreferences at the top of your adapter class
    private val sharedPreferences = context.getSharedPreferences("LikedStatus", Context.MODE_PRIVATE)

    // ViewHolder class holds references to the views in each list item
    class MyViwHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtShayari: TextView = v.findViewById(R.id.txtShayari)
        var imgCopy: LinearLayout = v.findViewById(R.id.imgCopy)
        var llLike: LinearLayout = v.findViewById(R.id.llLike)
        var imgAdd: LinearLayout = v.findViewById(R.id.imgAdd)
        var imgShare: LinearLayout = v.findViewById(R.id.imgShare)
        var imgTapChange: ImageView = v.findViewById(R.id.imgTapChange)
        var imgLike: ImageView = v.findViewById(R.id.imgLike)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViwHolder {
        // Inflate the layout for each Shayari item
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.display_shayari_itemfile, parent, false)
        return MyViwHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViwHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view with that element
        with(holder) {
            // Get the Shayari text for the current position
            val shayariText = list[position].Shayari
            txtShayari.text = shayariText

            // Set up click listeners for the copy and share buttons
            imgCopy.setOnClickListener {
                // Call the copyToClipboard function with the Shayari text
                copyToClipboard(it.context, shayariText)
            }

            imgAdd.setOnClickListener {
                // Invoke the showcase function with the clicked Shayari
                showcase.invoke(list[position].Shayari)
            }

            imgShare.setOnClickListener {
                // Call the shareQuote function with the Shayari text
                shareQuote(it.context, shayariText)
            }

            // Set up click listener for the like button
            llLike.setOnClickListener {
                // Toggle the like status
                list[position].Status = if (list[position].Status == 1) 0 else 1

                // Update the UI
                val updatedLikeStatusResource =
                    if (list[position].Status == 1) R.drawable.heart_pink else R.drawable.heart
                imgLike.setImageResource(updatedLikeStatusResource)

                // Add logs
                val likeStatus = if (list[position].Status == 1) "Liked" else "Unliked"
                Log.d(
                    "LikeStatus",
                    "$likeStatus - Position: $position, ShayariID: ${list[position].ShayariID}"
                )

                // Update the like status in SharedPreferences
                sharedPreferences.edit()
                    .putInt("liked_${list[position].ShayariID}", list[position].Status).apply()

                // Update the like status in the database
                updateLikeStatusInDatabase(list[position].ShayariID, list[position].Status)
            }

            // Retrieve and set the like status from SharedPreferences
            val storedLikeStatus =
                sharedPreferences.getInt("liked_${list[position].ShayariID}", 0)
            list[position].Status = storedLikeStatus
            val currentLikeStatusResource =
                if (storedLikeStatus == 1) R.drawable.heart_pink else R.drawable.heart
            imgLike.setImageResource(currentLikeStatusResource)

            imgTapChange.setOnClickListener {
                // Increment the background index and set the new background
                currentBackgroundIndex = (currentBackgroundIndex % 12) + 1
                val backgroundResource = context.resources.getIdentifier(
                    "g$currentBackgroundIndex",
                    "drawable",
                    context.packageName
                )
                imgTapChange.setImageResource(backgroundResource)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return list.size
    }

    // Update the like status in the database
    private fun updateLikeStatusInDatabase(shayariID: Int, status: Int) {
        val dbHelper = MyDatabaseHelper(context, "ShayariDB.db")
        dbHelper.updateLikeStatus(shayariID, status)
    }

    // Copy Shayari text to the clipboard
    private fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Quote", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Shayari copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    // Share Shayari text using an Intent
    private fun shareQuote(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }
}
