package com.example.shayari.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shayari.R
import com.example.shayari.databinding.ActivityShayariShowcaseBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException

class ShayariShowcaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShayariShowcaseBinding
    var CAMERA_REQUEST = 100
    var SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityShayariShowcaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the views and set up event listeners
        initview()
    }

    private fun initview() {
        with(binding) {
            // Get the Shayari text from the Intent
            var Shayari = intent.getStringExtra("Shayari")
            // Set the Shayari text to the TextView
            txtShayari.text = Shayari

            // Set up click listeners for the copy and share buttons
            imgCopy.setOnClickListener {
                if (Shayari != null) {
                    // Call the copyToClipboard function with the Shayari text
                    copyToClipboard(it.context, Shayari)
                }
            }

            imgShare.setOnClickListener {
                if (Shayari != null) {
                    // Call the shareQuote function with the Shayari text
                    shareQuote(it.context, Shayari)
                }
            }

            // Set up click listener for adding an image
            imgAddImage.setOnClickListener {
                // Show the custom dialog for image selection
                showCustomDilougeBox()
            }

            // Set up click listener for downloading the Shayari showcase
            binding.imgDownload.setOnClickListener {
                // Capture the drawing cache of the specified views
                val backgroundBitmap = getBitmapFromView(binding.imgBackground)
                val textBitmap = getBitmapFromView(binding.txtShayari)

                // Combine the backgroundBitmap and textBitmap to create the final image
                val finalBitmap = combineBitmaps(backgroundBitmap, textBitmap)

                // Save the final image to the gallery
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    finalBitmap,
                    "image_title",
                    "image_description"
                )

                Toast.makeText(
                    this@ShayariShowcaseActivity,
                    "Download Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun combineBitmaps(backgroundBitmap: Bitmap, textBitmap: Bitmap): Bitmap {
        // Combine two bitmaps into a single bitmap
        val combinedBitmap = Bitmap.createBitmap(
            backgroundBitmap.width,
            backgroundBitmap.height,
            backgroundBitmap.config
        )

        val canvas = Canvas(combinedBitmap)
        canvas.drawBitmap(backgroundBitmap, 0f, 0f, null)

        // Calculate the center position for the text
        val centerX = (backgroundBitmap.width - textBitmap.width) / 2.toFloat()
        val centerY = (backgroundBitmap.height - textBitmap.height) / 2.toFloat()

        // Draw the text on top of the background
        canvas.drawBitmap(textBitmap, centerX, centerY, null)

        return combinedBitmap
    }

    // Function to capture the drawing cache of a specific view
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun showCustomDilougeBox() {
        // Show a custom dialog for image selection
        val dialog = BottomSheetDialog(this@ShayariShowcaseActivity)
        dialog.setContentView(R.layout.image_picker_item)
        val Imggallery = dialog.findViewById<ImageView>(R.id.Imggallery)
        val imgCamera = dialog.findViewById<ImageView>(R.id.imgCamera)

        Imggallery!!.setOnClickListener {
            dialog.dismiss()
            // Open the gallery for image selection
            val i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
        }

        imgCamera!!.setOnClickListener {
            dialog.dismiss()
            // Open the camera for capturing an image
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, CAMERA_REQUEST)
        }

        // Set background drawable and show the dialog
        dialog.window!!.setBackgroundDrawable(ColorDrawable(6))
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    // Update the preview image in the layout with the selected image
                    binding.imgBackground.setImageURI(selectedImageUri)
                }
            }
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                // Get the captured photo and set it to the background image
                val photo = data?.extras?.get("data") as Bitmap?
                binding.imgBackground.setImageBitmap(photo)
            }
        }
    }

    private fun copyToClipboard(context: Context, text: String) {
        // Copy the Shayari text to the clipboard
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Quote", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "Quote copied to clipboard !", Toast.LENGTH_SHORT).show()
    }

    private fun shareQuote(context: Context, text: String) {
        // Share the Shayari text using Intent.ACTION_SEND
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }
}
