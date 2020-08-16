package com.developer2t.mygithubuserfinal.view.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.adapter.SectionPagerAdapter
import com.developer2t.mygithubuserfinal.model.User
import com.developer2t.mygithubuserfinal.viewmodel.ViewModelUserDetail
import kotlinx.android.synthetic.main.content_detail_user.*
import kotlinx.android.synthetic.main.content_detail_user.tv_username
import kotlinx.android.synthetic.main.list_user.*

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_USER"
    }

    private lateinit var vmUserDetail: ViewModelUserDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
//        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

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