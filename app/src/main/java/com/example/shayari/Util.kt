    package com.example.shayari

    // Represents a category in the Shayari app
    data class categoryModal(val CategoryID : Int , val CategoryName : String , var CategoryImage:String="")

    // Represents a Shayari (poetry) in the Shayari app
    data class shayariModal(val ShayariID : Int , val Shayari : String , var CategoryID : Int , var Status: Int)

    // Represents a favorite Shayari in the Shayari app
    data class favouriteModal (var ShayariID : Int,var Shayari: String,var Status: Int)
