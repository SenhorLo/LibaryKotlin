package com.example.trabalhofinal.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.R
import com.example.trabalhofinal.databinding.ActivityUserBinding
import com.google.android.material.tabs.TabLayout

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("USERNAME")
        
        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        val isAdmin = intent.getBooleanExtra("IS_ADMIN", false)
        if (isAdmin) {
            binding.btnBackToAdmin.visibility = android.view.View.VISIBLE
            binding.btnBackToAdmin.setOnClickListener { finish() }
            // Opcional: Se for admin vindo da busca, selecionar a aba de busca
            binding.tabLayout.getTabAt(1)?.select()
            replaceFragment(SearchFragment())
        } else {
            // Initial fragment
            replaceFragment(LibraryFragment())
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceFragment(LibraryFragment())
                    1 -> replaceFragment(SearchFragment())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(com.example.trabalhofinal.R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            com.example.trabalhofinal.R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        val intent = Intent(this, com.example.trabalhofinal.ui.login.LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("USERNAME", username)
        fragment.arguments = bundle
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
