package com.example.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.example.baseclasses.BaseActivity
import com.example.data.model.Product
import com.example.demoproject.BottomSheetFragment
import com.example.demoproject.R
import com.example.demoproject.databinding.ActivityMainBinding
import com.example.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>(), MainPresenter {

    private var productList: MutableList<Product> = ArrayList()

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override val viewModel: MainViewModel by viewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = setDataBindingView(R.layout.activity_main)

        binding.productAdapter = ProductAdapter(this, productList, this)

        binding.presenter = this

        viewModel.getDataFromDatabase()

        viewModel.getData.observe(this) {
            productList.clear()
            productList.addAll(it)
            binding.productAdapter?.notifyDataSetChanged()
            dismissProgressDialog()
        }


    }

    override fun onItemClick(list: Product) {
        viewModel.updateDataFromDatabase(list)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onWelComeClick() {
        /*val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)*/

        BottomSheetFragment.show(supportFragmentManager)
    }

    private fun setupToolbar(title: String, navigationIcon: Drawable? = null) {
//        binding.toolbar.topAppBar.title = title
//        binding.toolbar.topAppBar.navigationIcon = navigationIcon
    }

    private fun updateTopBar(
        fragmentId: Int
    ) {
        when (fragmentId) {


        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /*private fun setupBottomNavigationBar(notificationIntent: Intent?) {

        // all bottom navigation fragment id  = navGraphIds
        val navGraphIds =
            listOf(
                R.navigation.nav_customer_dashboard,
                R.navigation.nav_customer_business,
                R.navigation.nav_settings
            )

        // fragmentContainer = androidx.fragment.app.FragmentContainerView = <<<< id
        val controller = binding.bottomNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragmentContainer,
            intent = intent
        )
        controller.observe(this) { navController1 ->
            this.navController = navController1

            navController1.addOnDestinationChangedListener { _, destination, _ ->
                Log.d("navigate", "navigated to -----> " + destination.label)
                updateTopBar(destination.id)
            }
        }
    }*/

}