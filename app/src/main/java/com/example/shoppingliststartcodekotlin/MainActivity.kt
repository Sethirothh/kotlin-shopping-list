package com.example.shoppingliststartcodekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingliststartcodekotlin.adapters.ProductAdapter
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.example.shoppingliststartcodekotlin.data.Repository.addProduct
import com.example.shoppingliststartcodekotlin.data.Repository.deleteAllProducts
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
   lateinit var adapter: ProductAdapter
    private val items = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun addNewProduct(){

        val newProduct = Product(
            name = editTextTitle.text.toString(),
            quantity = editTextQuantity.text.toString().toInt(),
            price = editTextPrice.text.toString(),
        )
        addProduct(newProduct)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(applicationContext)
        button_add.setOnClickListener{ addNewProduct()}

        Repository.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI()
        })


        sortNameButton.setOnClickListener {
            Repository.products.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }

        sortQuantityButton.setOnClickListener {
            Repository.products.sortByDescending { it.quantity }
            adapter.notifyDataSetChanged()
        }

        //Spinner
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, items)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            //The AdapterView<?> type means that this can be any type,
            //so we can use both AdapterView<String> or any other
            override fun onItemSelected(adapterView: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                //So this code is called when ever the spinner is clicked
                Toast.makeText(this@MainActivity,
                    "Item selected: " + items[position], Toast.LENGTH_SHORT)
                    .show()

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
                // you would normally do something here
                // for instace setting the selected item to "null"
                // or something.
            }
        }
    }


    fun updateUI() {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

       recyclerView.layoutManager = layoutManager

       adapter = ProductAdapter(Repository.products)

      /*connecting the recyclerview to the adapter  */
        recyclerView.adapter = adapter

    }

    fun convertListToString(): String
    {
        var result = ""
        for (product in Repository.products)
        {
            result = result + product.toString()
        }
        return result
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_about -> {
                /* Share content */
                val text = convertListToString() //from EditText
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain" //MIME-TYPE
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Data")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(sharingIntent, "Share Using"))
                return true
            }
            R.id.item_delete -> {
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_LONG)
                    .show()
                Repository.deleteAllProducts();
                return true
            }
            R.id.item_help -> {
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_refresh -> {
                Toast.makeText(this, "Refresh item clicked!", Toast.LENGTH_LONG)
                    .show()
                finish();
                startActivity(getIntent());
                return true
            }
        }

        return false //we did not handle the event

    }
}