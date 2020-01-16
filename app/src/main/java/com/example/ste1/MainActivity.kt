package com.example.ste1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ste1.databinding.NavHeaderMainBinding
import com.example.ste1.ui.header.HeaderViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var headerBinding:NavHeaderMainBinding
    private lateinit var auth: FirebaseAuth
    val TAG ="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(auth.currentUser==null){
            auth.signInAnonymously().addOnSuccessListener {

            }
        }


        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        headerBinding = NavHeaderMainBinding.inflate(layoutInflater, navView, false).apply {
            lifecycleOwner = this@MainActivity
            vm = ViewModelProviders.of(this@MainActivity).get(HeaderViewModel::class.java)
        }
        headerBinding.headerLinearLayout.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null||FirebaseAuth.getInstance().currentUser?.isAnonymous!!) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login)
            } else {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
            }
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
        }
        FirebaseAuth.getInstance().addAuthStateListener {
            if (FirebaseAuth.getInstance().currentUser==null||FirebaseAuth.getInstance().currentUser?.isAnonymous!!) {
                headerBinding.textViewHeaderTitle.text = resources.getText(R.string.nav_header_title)
                headerBinding.textViewHeaderDes.text= resources.getText(R.string.click_to_login)
            } else {
                Log.d("MainActivity","logined")
                headerBinding.textViewHeaderTitle.text =FirebaseAuth.getInstance().currentUser?.displayName
                headerBinding.textViewHeaderDes.text=FirebaseAuth.getInstance().currentUser?.email
            }
        }
        navView.addHeaderView(headerBinding.root)
        val navController = findNavController(R.id.nav_host_fragment)
       // navView.add
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_scan
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings->{


            }

        }

        return true


    }
//    fun navigateToLoginOrProfile(view: View) {
//        if (FirebaseAuth.getInstance().currentUser == null) {
//            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login)
//        } else {
//            findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
//        }
//        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
//    }


//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//       // updateUI(currentUser)
//    }










}


