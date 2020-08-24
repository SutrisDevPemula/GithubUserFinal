package com.developer2t.mygithubuserfinal.view.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.adapter.SectionPagerAdapter
import com.developer2t.mygithubuserfinal.database.DatabaseContract
import com.developer2t.mygithubuserfinal.database.UserHelper
import com.developer2t.mygithubuserfinal.model.User
import com.developer2t.mygithubuserfinal.viewmodel.ViewModelUserDetail
import kotlinx.android.synthetic.main.content_detail_user.*
import kotlinx.android.synthetic.main.content_detail_user.tv_username

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_USER"
    }

    private lateinit var vmUserDetail: ViewModelUserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
//        setSupportActionBar(findViewById(R.id.toolbar))


        val usernameQ = intent.getStringExtra(EXTRA_USER)

        vmUserDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelUserDetail::class.java)
        showLoading(true)

        vmUserDetail.setDetailUser(usernameQ.toString())
        vmUserDetail.getDetailUser().observe(this, Observer { detailUser ->
            if (detailUser != null) {
                setDetailUser(detailUser)
                addToFavorite(detailUser)
                showLoading(false)
            }

        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

    private fun addToFavorite (user : User) {
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val userHelper = UserHelper(this@DetailUser)
            userHelper.open()

            val values = ContentValues()
            values.put(DatabaseContract.UserColumns.ID, user.id)
            values.put(DatabaseContract.UserColumns.NAME, user.username)
            values.put(DatabaseContract.UserColumns.PHOTO, user.images)

            val result = userHelper.insert(values)
            if (result > 1) {
                Toast.makeText(
                    this,
                    "${user.username} berhasil ditambakan ke daftar favorite",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "User ini sudah ada di dalam favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
            userHelper.close()
        }
    }


    private fun setDetailUser(users: User) {
        Glide.with(this)
            .load(users.images)
            .apply(RequestOptions()).override(120, 120)
            .into(img_item_photo)

        tv_username.text = users.username
        tv_name.text = users.name
        tv_location.text = users.location
        tv_company.text = users.company
        tv_repositori.text = users.repo.toString()
        tv_followers.text = users.follower.toString()
        tv_followings.text = users.followings.toString()

        val sectionPagerAdapter = SectionPagerAdapter(
            this, supportFragmentManager, users.username
        )
        view_pager.adapter = sectionPagerAdapter
        tabs_layout.setupWithViewPager(view_pager)
    }
}