package com.example.shoppingliststartcodekotlin

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


//extending the standard preference fragment
class MyPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs, rootKey)
    }


}