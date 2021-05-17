package com.example.shoppingliststartcodekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingliststartcodekotlin.adapters.ProductAdapter
import com.example.shoppingliststartcodekotlin.data.Repository

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
   lateinit var adapter: ProductAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Repository.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI()
        })
    }


    fun updateUI() {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

       // recyclerView.layoutManager = layoutManager

       adapter = ProductAdapter(Repository.products)

      /*connecting the recyclerview to the adapter  */
      //  recyclerView.adapter = adapter

    }
}