package com.example.afinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.afinal.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mine.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->{
                    loadFragment(HomeFragment())
                    title = resources.getString(R.string.home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_stuff->{
                    loadFragment(StuffFragment())
                    title = resources.getString(R.string.stuff)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_sold->{
                    loadFragment(SoldFragment())
                    title = resources.getString(R.string.sold)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_mine->{
                    loadFragment(MineFragment())
                    title = resources.getString(R.string.mine)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_news->{
                    loadFragment(NewsFragment())
                    title = "资讯"
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
