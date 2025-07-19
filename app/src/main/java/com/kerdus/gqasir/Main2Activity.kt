package com.kerdus.gqasir

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.core.content.ContextCompat
import com.kerdus.gqasir.databinding.ActivityMain2Binding
import com.kerdus.gqasir.ui.ViewModelFactory
import com.kerdus.gqasir.ui.login.LoginActivity

class Main2Activity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        setSupportActionBar(binding.appBarMain2.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_warehouse, R.id.nav_assignment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logoutButton -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main2)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}