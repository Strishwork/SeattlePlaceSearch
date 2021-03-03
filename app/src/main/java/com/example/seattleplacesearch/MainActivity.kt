package com.example.seattleplacesearch

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.seattleplacesearch.fragments.SearchFragment
import com.example.seattleplacesearch.fragments.VenueDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchFragment.ItemClickedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment.newInstance())
                .commit()
        }
    }

    override fun itemClicked(venue: Venue) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, VenueDetailsFragment.newInstance(venue))
            .addToBackStack(null)
            .commit()
        displayHomeButtonAsUp(true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            displayHomeButtonAsUp(false)
        } else {
            super.onBackPressed()
        }
    }

    private fun displayHomeButtonAsUp(state: Boolean){
        supportActionBar?.setDisplayHomeAsUpEnabled(state)
        supportActionBar?.setDisplayShowHomeEnabled(state)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}