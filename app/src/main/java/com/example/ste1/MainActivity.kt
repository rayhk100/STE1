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
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableList
import androidx.databinding.ObservableMap
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ste1.databinding.NavHeaderMainBinding
import com.example.ste1.ui.detail.DetailItemViewModel
import com.example.ste1.ui.header.HeaderViewModel
import com.example.ste1.ui.list.ItemViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.detail_item_fragment.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var headerBinding: NavHeaderMainBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        val db = FirebaseFirestore.getInstance()
        val TAG = "MainActivity"

        val AllProduct = HashMap<String, DetailItemViewModel>()
        val AllProduct2 = HashMap<String, Product>()
//        val AllProduct2 = ObservableMap<String, DetailItemViewModel>()
//   val AllProduct3 = AllProduct:ObservableList<DetailItemViewModel>(){}

//            val AllProduct3 =BaseObservable()






    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        //val product = db.collection("Product1")
        db.collection("Product1").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            AllProduct.clear()
            AllProduct2.clear()


            querySnapshot?.documents?.forEach { doc ->
//                HashMaping
                val nutri= db.collection("Product1").document(doc.id).collection("nutri")

                AllProduct.set(doc.reference.path, DetailItemViewModel()).apply{
                AllProduct[doc.reference.path]!!.product.value=doc.getString("name").toString()

                    nutri.document("energy").addSnapshotListener(){
                        documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.energy.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "energy"+AllProduct[doc.reference.path]!!.energy.value.toString())
                    }
                    nutri.document("protein").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.protein.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "protein"+AllProduct[doc.reference.path]!!.protein.value.toString())
                    }
                    nutri.document("fat").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.sfat.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "fat"+AllProduct[doc.reference.path]!!.fat.value.toString())
                    }
                    nutri.document("S fat").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.sfat.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "sfat"+AllProduct[doc.reference.path]!!.sfat.value.toString())
                    }
                    nutri.document("T fat").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.tfat.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "tfat"+AllProduct[doc.reference.path]!!.tfat.value.toString())
                    }
                    nutri.document("carbo").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.carbo.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "carbo"+AllProduct[doc.reference.path]!!.carbo.value.toString())
                    }
                    nutri.document("sugar").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.sugar.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "sugar"+AllProduct[doc.reference.path]!!.sugar.value.toString())
                    }
                    nutri.document("sodium").addSnapshotListener(){
                            documentSnapshot, firebaseFirestoreException ->
                        AllProduct[doc.reference.path]!!.sodium.value = documentSnapshot?.getDouble("value")
//                        Log.d("Main_ALLN", documentSnapshot?.getDouble("value").toString())
                        Log.d("Main_ALLN", "sodium"+AllProduct[doc.reference.path]!!.sodium.value.toString())
                    }


            }


                db.collection("Product1").document(doc.id).collection("nutri").addSnapshotListener { snapshot, firebaseFirestoreException ->
                    val product = Product(doc.getString("name")!!,


                            snapshot?.documents?.map { item ->item.getDouble("value")!!}!!  )


                    AllProduct2.set(doc.reference.path,product)
//                    Log.d("Main_ALL1",AllProduct2[doc.reference.path]!!._productName)
//                    Log.d("Main_ALL1",AllProduct2[doc.reference.path]!!.nutri.toString())

                }





//                Log.d("Main_ALL",doc.reference.path)
//                Log.d("Main_ALL",AllProduct[doc.reference.path]!!.product.value.toString())
            }




        }

        AndroidThreeTen.init(this)


//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        headerBinding = NavHeaderMainBinding.inflate(layoutInflater, navView, false).apply {
            lifecycleOwner = this@MainActivity
            vm = ViewModelProviders.of(this@MainActivity).get(HeaderViewModel::class.java)
        }
        headerBinding.headerLinearLayout.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser == null || FirebaseAuth.getInstance().currentUser?.isAnonymous!!) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_login)
            } else {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
            }
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawers()
        }
        FirebaseAuth.getInstance().addAuthStateListener {
            if (auth.currentUser == null) {
                auth.signInAnonymously().addOnSuccessListener {result ->
//                Log.d("Main_Anony",result.toString())
                }
            }
            if (FirebaseAuth.getInstance().currentUser == null || FirebaseAuth.getInstance().currentUser?.isAnonymous!!) {
                headerBinding.textViewHeaderTitle.text =
                    resources.getText(R.string.nav_header_title)
                headerBinding.textViewHeaderDes.text = resources.getText(R.string.click_to_login)
            } else {
                Log.d("MainActivity", "logined")
                headerBinding.textViewHeaderTitle.text =
                    FirebaseAuth.getInstance().currentUser?.displayName
                headerBinding.textViewHeaderDes.text = FirebaseAuth.getInstance().currentUser?.email
            }
        }
        navView.addHeaderView(headerBinding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        // navView.add
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,  R.id.nav_list
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
        when (item.itemId) {
            R.id.action_settings -> {


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


