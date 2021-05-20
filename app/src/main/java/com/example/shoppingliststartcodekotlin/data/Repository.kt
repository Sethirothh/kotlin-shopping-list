package com.example.shoppingliststartcodekotlin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object Repository {
    var products = mutableListOf<Product>()
    private lateinit var db: FirebaseFirestore

    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
            //data-fetch
            addRealTimeListener()

            //get-test-data
            //createTestData()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    fun addProduct(product:Product) {
        //making the firestore val
        db = Firebase.firestore
        val docRef = db.collection("products")
        //Product Add to List
        products.add(product)
        productListener.value = products
        //Product add to collection
        docRef.add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
                product.id = documentReference.id            }
            .addOnFailureListener{ e ->
                Log.w("Error", "Error adding document", e)
            }
    }

    fun deleteAllProducts(): MutableLiveData<MutableList<Product>>{
        db = Firebase.firestore
        val docRef = db.collection("products")
        productListener.value = products
        for (product in products){
            docRef.document(product.id).delete().addOnSuccessListener {
                Log.d("Snapshot","DocumentSnapshot with id: ${product.id} successfully deleted!")
                // products.removeAt(index) //removes it from the list
            }
                .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
        }
        products.clear()
        productListener.value = products
        return productListener

    }
    fun deleteProduct(index: Int) {
        db = Firebase.firestore
        val docRef = db.collection("products")
        val product = products[index]
        docRef.document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot","DocumentSnapshot with id: ${product.id} successfully deleted!")
            //products.removeAt(index) //removes it from the list
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
    }

    fun readDataFromFireBase()
    {
        val db = Firebase.firestore
        val docRef = db.collection("products")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Repository", "${document.id} => ${document.data}")
                    val product = document.toObject<Product>()
                    product.id = document.id  //set the ID in the product class
                    products.add(product)
                }
                productListener.value = products //notify our listener we have new data
            }
            .addOnFailureListener { exception ->
                Log.d("Repository", "Error getting documents: ", exception)
            }
    }

    //Testing purposes
    fun createTestData()
    {
        val product1 = Product("Pasta", 0, 0, )
        val product2 = Product("Pasta", 0, 0, )
        val product3 = Product("Pasta", 0, 0, )


        //add some products to the products list - for testing purposes
        products.add(product1)
        products.add(product2)
        products.add(product3)
    }
    private fun addRealTimeListener()
    {
        val db = Firebase.firestore
        val docRef = db.collection("products")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {  //any errors
                Log.d("Repository", "Listen failed.", e)
                return@addSnapshotListener
            }
            products.clear() //to avoid duplicates.
            for (document in snapshot?.documents!!) { //add all products to the list
                Log.d("Repository_snapshotlist", "${document.id} => ${document.data}")
                val product = document.toObject<Product>()!!
                product.id = document.id
                products.add(product)
            }

            productListener.value = products
        }
    }

}