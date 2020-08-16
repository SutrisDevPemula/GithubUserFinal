package com.developer2t.mygithubuserfinal.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.adapter.CardUserFavAdapter
import com.developer2t.mygithubuserfinal.database.UserHelper
import com.developer2t.mygithubuserfinal.helper.MappingHelper
import com.developer2t.mygithubuserfinal.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUser : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    private lateinit var adapter: CardUserFavAdapter
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)


        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userHelper = UserHelper.getInstance(this)
        userHelper.open()

        showRecyclerCardView()

        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null)
                adapter.setData(list)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData())
    }

    private fun showRecyclerCardView() {
        adapter = CardUserFavAdapter(this)
        adapter.notifyDataSetChanged()

        rv_item.layoutManager = LinearLayoutManager(this)
        rv_item.setHasFixedSize(true)
        rv_item.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)

            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapUserCursorToArrayList(cursor)
            }

            showLoading(false)

            val user = deferredUser.await()
            if (user.size > 0) {
                adapter.setData(user)
            } else {
                adapter.setData(ArrayList())
                val message = resources.getString(R.string.no_data_favorite_user)
                Snackbar.make(rv_item, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}