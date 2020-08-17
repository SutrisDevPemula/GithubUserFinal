package com.developer2t.mygithubuserfinal

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer2t.mygithubuserfinal.adapter.CardUserAdapter
import com.developer2t.mygithubuserfinal.settings.SettingsUser
import com.developer2t.mygithubuserfinal.view.activity.FavoriteUser
import com.developer2t.mygithubuserfinal.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

    class MainActivity : AppCompatActivity() {

        private lateinit var adapter: CardUserAdapter
        private lateinit var userViewModel: UserViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

        supportActionBar?.title = "GithubUser"

        showRecyclerCardView()

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)
        showLoading(true)
        userViewModel.setUsers(UserViewModel.LIST)

        userViewModel.getUsers().observe(this, Observer { listItem ->
            if (listItem != null) {
                adapter.setData(listItem)
                showLoading(false)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                userViewModel.setUsers(UserViewModel.SEARCH, query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    showLoading(true)
                    userViewModel.setUsers(UserViewModel.LIST)
                } else {
                    showLoading(true)
                    userViewModel.setUsers(UserViewModel.SEARCH, query)
                }
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteUser::class.java)
                startActivity(intent)

            }
            R.id.menu_setting -> {
                val intent = Intent(this, SettingsUser::class.java)
                startActivity(intent)
            }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun showRecyclerCardView() {
        adapter = CardUserAdapter(this)
        adapter.notifyDataSetChanged()

        rv_item.layoutManager = LinearLayoutManager(this)
        rv_item.adapter = adapter

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}