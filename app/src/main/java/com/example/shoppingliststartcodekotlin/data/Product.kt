package com.example.shoppingliststartcodekotlin.data

import com.google.firebase.firestore.Exclude

data class Product(var name:String = "", var price:String="", var quantity:Int=0, @get:Exclude var id: String = "") {

    override fun toString(): String {
        return "Product: $name - $quantity - $price kr \n"
    }
}
