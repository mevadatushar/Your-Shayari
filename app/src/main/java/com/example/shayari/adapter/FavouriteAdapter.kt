package com.example.shayari.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shayari.R
import com.example.shayari.favouriteModal

// Adapter for displaying favorite Shayari in a RecyclerView
class FavouriteAdapter(
    var context: Context,
    var list: ArrayList<favouriteModal>,
    var like: (Int, Int) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    // Start with the first background (g1)
    private var currentBackgroundIndex = 1

    // ViewHolder class holds references to the views in each list item
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgTapChange: ImageView = view.findViewById(R.id.imgTapChange)
        var imgCopy: LinearLayout = view.findViewById(R.id.imgCopy)
        var imgShare: LinearLayout = view.findViewById(R.id.imgShare)
        var txtShayari: TextView = view.findViewById(R.id.txtShayari)
        var imglike: ImageView = view.findViewById(R.id.imgLike)
        var llLike: LinearLayout = view.findViewById(R.id.llLike)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate the layout for each favorite Shayari item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_itemfile, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view with that element
        with(holder) {
            // Get the Shayari text for the current position
            val shayariText = list[position].Shayari
            txtShayari.text = shayariText

            // Set up click listener for the unlike button
            llLike.setOnClickListener {
                // Invoke the 'like' function with ShayariID and status parameters
                like.invoke(list[position].ShayariID, 0)
                imglike.setImageResource(R.drawable.heart)
                list[position].Status = 0
                // Delete the item from the RecyclerView
                DeleteItem(position)
            }

            // Set up click listeners for the copy and share buttons
            imgCopy.setOnClickListener {
                // Call the copyToClipboard function with the Shayari text
                copyToClipboard(it.context, shayariText)
            }

            imgShare.setOnClickListener {
                // Call the shareQuote function with the Shayari text
                shareQuote(it.context, shayariText)
            }

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

    // Update the dataset with a new list
    fun UpdateList(list: ArrayList<favouriteModal>) {
        this.list = list
        notifyDataSetChanged()
    }

    // Delete an item from the dataset at a specific position
    fun DeleteItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
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
