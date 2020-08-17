package com.developer2t.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer2t.consumerapp.adapter.CardUserFavAdapter
import com.developer2t.consumerapp.database.DatabaseContract
import com.developer2t.consumerapp.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.developer2t.consumerapp.helper.MappingHelper
import com.developer2t.consumerapp.model.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: CardUserFavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer User Fav"

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(
            CONTENT_URI,
            true,
            myObserver
        )


        showRecyclerCardView()

        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null)
                adapter.setData(list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData())
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.IO) {
            showLoading(true)

            val deferredUser = async(Dispatchers.IO) {
                val cursor = this@MainActivity.contentResolver.query(
                    DatabaseContract.UserColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
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
}
