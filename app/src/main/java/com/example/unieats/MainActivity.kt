package com.example.unieats

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unieats.models.History
import com.example.unieats.models.User
import com.example.unieats.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        var history = History(0,0, 0)
        var histMap = mapOf<String, History>("" to history)
        var selectedUser = User("","","", histMap, "", "", "0")
        var locationId : Int = -1

        var firstName : String = ""
        var lastName : String = ""
        var username : String = ""
        var password : String = ""
        var email : String = ""
        var uni : String = ""

        fun reset () {
            firstName = ""
            lastName = ""
            username = ""
            password = ""
            email = ""
            uni = ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_search, R.id.navigation_log,
            R.id.navigation_settings, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var intentLocation = intent.extras?.getInt("chosenId")
        var intentFragment = intent.extras?.getInt("fragmentLoad");

        when(intentFragment) {

            0 -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_navigation_home_to_navigation_search)
            }

        }

        if (intentLocation != null) {
           /* val bundle = Bundle()
            bundle.putInt("chosenId", intentLocation)
            var searchPass = SearchFragment
            searchPass.arguments = bundle
            Log.e("LOCATIONINMAIN", intentLocation.toString())*/
            locationId = intentLocation
        }

    }
}

