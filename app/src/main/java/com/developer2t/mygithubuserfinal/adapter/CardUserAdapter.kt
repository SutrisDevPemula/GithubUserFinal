package com.developer2t.mygithubuserfinal.adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.database.DatabaseContract
import com.developer2t.mygithubuserfinal.database.UserHelper
import com.developer2t.mygithubuserfinal.model.User
import com.developer2t.mygithubuserfinal.view.activity.DetailUser
import kotlinx.android.synthetic.main.list_user.view.*

class CardUserAdapter(private val context: Context) :
    RecyclerView.Adapter<CardUserAdapter.CardViewViewHolder>() {

    private val mUser = ArrayList<User>()

    fun setData(item: ArrayList<User>) {
        mUser.clear()
        mUser.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = mUser.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(mUser[position])
    }


    class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(users: User) {
            with(itemView) {
                Glide.with(itemView.context).load(users.images)
                    .apply(RequestOptions().override(65, 65)).into(user_images)
                tv_username.text = users.username
                tv_id.text = users.id.toString()

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailUser::class.java)
                    intent.putExtra(DetailUser.EXTRA_USER, users.username)
//                    intent.putExtra(FollowersFragment.EXTRA_USER, users.username)
                    ContextCompat.startActivity(context, intent, null)
                }

                btn_set_favorite.setOnClickListener {
                    val userHelper = UserHelper(itemView.context)
                    userHelper.open()
//
                    val values = ContentValues()
                    values.put(DatabaseContract.UserColumns.ID, users.id)
                    values.put(DatabaseContract.UserColumns.NAME, users.username)
                    values.put(DatabaseContract.UserColumns.PHOTO, users.images)

                    val result = userHelper.insert(values)
                    if (result > 0) {
                        Toast.makeText(
                            itemView.context,
                            "${users.username} berhasil ditambakan ke favorite",
                            Toast.LENGTH_SHORT
                        ).show()
//                        sendUpdate(context)
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "${users.username} ini sudah ada di favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    userHelper.close()
                }
            }
        }

    }


}