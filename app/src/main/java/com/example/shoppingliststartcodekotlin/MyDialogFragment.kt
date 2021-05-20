package org.pondar.dialogfragmentdemokotlinnew

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.shoppingliststartcodekotlin.R

//notice that the negClick has default value of the empty function
open class MyDialogFragment(var posClick: ()-> Unit, var negClick: ()->Unit= {}) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Here we create a new dialogbuilder;
        val alert = AlertDialog.Builder(
            activity)
        alert.setTitle(R.string.confirmation)
        alert.setMessage(R.string.areYouSure)
        alert.setPositiveButton(R.string.yes, pListener)
        alert.setNegativeButton(R.string.no, nListener)
        return alert.create()
    }

    //This is our positive listener for when the user presses
    //the yes button
    private var pListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener {_, _ ->
        // these will be executed when user click Yes button
        posClick()
    }

    //This is our negative listener for when the user presses
    //the no button.
    private var nListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, _ ->
        // these will be executed when user click No button
        negClick()
    }

}