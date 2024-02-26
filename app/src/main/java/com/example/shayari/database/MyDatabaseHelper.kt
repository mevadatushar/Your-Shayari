package com.example.shayari.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.shayari.categoryModal
import com.example.shayari.favouriteModal
import com.example.shayari.shayariModal
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

// Database helper class for Shayari application
class MyDatabaseHelper(var context: Context, var name: String) : SQLiteOpenHelper(context, name, null, 1) {

    // Database path and name
    var DB_PATH: String = ""
    var divider = "/"
    var DB_NAME: String = "ShayariDB.db"

    // Check if the database exists
    fun isDataBaseExists(): Boolean {
        DB_PATH = divider + "data" + divider + "data" + divider + context.packageName + divider + "databases/"
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    // Import database from assets if it doesn't exist
    @Throws(IOException::class)
    fun importDataBaseFromAssets() {
        if (!isDataBaseExists()) {
            this.readableDatabase
            val myInput = context.assets.open(DB_NAME)
            val outFileName = DB_PATH + DB_NAME
            val myOutput: OutputStream = FileOutputStream(outFileName)
            val buffer = ByteArray(1024)
            var length: Int
            while (myInput.read(buffer).also { length = it } > 0) {
                myOutput.write(buffer, 0, length)
            }
            myOutput.flush()
            myOutput.close()
            myInput.close()
        }
    }

    // Retrieve Shayari data based on the selected category
    fun displayShayari(CategoryName: String?): ArrayList<shayariModal> {
        val list: ArrayList<shayariModal> = ArrayList()
        val db = readableDatabase
        val sql = "SELECT * FROM ShayariTB WHERE CategoryID IN (SELECT CategoryID FROM CategoryTB WHERE CategoryName = ?)"
        val cursor = db.rawQuery(sql, arrayOf(CategoryName))
        if (cursor.moveToFirst()) {
            do {
                val ShayariID = cursor.getInt(0)
                val Shayari = cursor.getString(1)
                val CategoryID = cursor.getInt(2)
                val Status = cursor.getInt(3)

                val model = shayariModal(ShayariID, Shayari, CategoryID, Status)
                list.add(model)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Read category data from the CategoryTB table
    fun readCategory(): ArrayList<categoryModal> {
        var list: ArrayList<categoryModal> = ArrayList()
        var db = readableDatabase
        var sql = "select * from CategoryTB"
        var cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                var CategoryID = cursor.getInt(0)
                var CategoryName = cursor.getString(1)
                var CategoryImage = cursor.getString(2)

                var model = categoryModal(CategoryID, CategoryName, CategoryImage)
                list.add(model)

                Log.d("TAG", "readCategory: $CategoryID $CategoryName $CategoryImage")

            } while (cursor.moveToNext())
        }
        return list
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Database creation logic (if needed) goes here
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Database upgrade logic (if needed) goes here
    }

    // Update like status in the ShayariTB table
    fun updateLikeStatus(shayariID: Int, status: Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("Status", status)
        db.update("ShayariTB", values, "ShayariID=?", arrayOf(shayariID.toString()))
        db.close()
    }

    // Retrieve favorited Shayari records from ShayariTB
    fun FavoriteDisplayRecord(): ArrayList<favouriteModal> {
        var DisplayList = ArrayList<favouriteModal>()

        val dbF = readableDatabase
        val Sql = "Select * from ShayariTB where Status = 1"
        val c = dbF.rawQuery(Sql, null)
        if (c.moveToFirst()) {
            do {
                var Shayari_id = c.getInt(0)
                var Shayari = c.getString(1)
                var fav = c.getInt(2)

                Log.e("TAG", "FavoriteDisplayRecord: $Shayari_id $Shayari")
                var shayarimodal = favouriteModal(Shayari_id, Shayari, fav)

                DisplayList.add(shayarimodal)
            } while (c.moveToNext())
        }
        return DisplayList
    }

    // Update like status in ShayariTB and delete the item from the RecyclerView
    fun UpdeteRecord(id: Int, Status: Int) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Status", Status)

        val whereClause = "ShayariID = ?"
        val whereArgs = arrayOf(id.toString())

        db.update("ShayariTB", contentValues, whereClause, whereArgs)
        db.close()
    }
}
