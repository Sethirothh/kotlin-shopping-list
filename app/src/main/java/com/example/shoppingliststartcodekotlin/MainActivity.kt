package com.example.shoppingliststartcodekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingliststartcodekotlin.adapters.ProductAdapter
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.example.shoppingliststartcodekotlin.data.Repository.addProduct
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*
import org.pondar.dialogfragmentdemokotlinnew.MyDialogFragment

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
   lateinit var adapter: ProductAdapter
    private val numbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun addNewProduct(){

        val newProduct = Product(
            name = editTextTitle.text.toString(),
            price = editTextPrice.text.toString().toInt(),
            quantity = editTextQuantity.text.toString().toInt(),
        )
        addProduct(newProduct)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(applicationContext)

        //Create Item Button
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
        sortPriceButton.setOnClickListener {
            Repository.products.sortByDescending { it.price }
            adapter.notifyDataSetChanged()
        }
        //Settings


    }

    //Settings
    private val RESULT_CODE_PREFERENCES = 1

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_PREFERENCES)
        //the code means we came back from settings
        {
            //I can call these methods like this, because they are static
            val male = PreferenceHandler.isMale(this)
            val name = PreferenceHandler.getName(this)
            var toastGender = "";
            if (male) {
                toastGender = resources.getString(R.string.male)
            } else {
                toastGender = resources.getString(R.string.female)
            }

            val message = "Welcome, $name, I can see that you're a real $toastGender";
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    // Options
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
            R.id.item_share -> {
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
                val dialog = MyDialogFragment(::positiveClicked, ::negativeClick)
                dialog.show(supportFragmentManager, "myFragment")

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
            R.id.action_settings -> {
                //Start our settingsactivity and listen to result - i.e.
                //when it is finished.
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, RESULT_CODE_PREFERENCES)

            }
        }

        return false //we did not handle the event

    }


    //callback function from yes/no dialog - for yes choice
    fun positiveClicked() {
        val toast = Toast.makeText(
            this,
            "All Items Deleted", Toast.LENGTH_LONG
        )
        toast.show()
        Repository.deleteAllProducts()
    }


    //callback function from yes/no dialog - for no choice
    fun negativeClick() {
        //Here we override the method and can now do something
        val toast = Toast.makeText(
            this,
            "No Items Deleted", Toast.LENGTH_LONG
        )
        toast.show()
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
}