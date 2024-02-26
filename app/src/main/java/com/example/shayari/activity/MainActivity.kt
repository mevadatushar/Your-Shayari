package com.example.shayari.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.shayari.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Using Handler to delay the launch of the CategoryActivity
        Handler().postDelayed({
            // This method will be executed once the timer is over

            // Creating an Intent to start the CategoryActivity
            val i = Intent(this@MainActivity, CategoryActivity::class.java)

            // Starting the CategoryActivity
            startActivity(i)

            // Finishing the current activity (MainActivity)
            finish()
        }, 1500) // Delay time in milliseconds (1.5 seconds)
    }
}
