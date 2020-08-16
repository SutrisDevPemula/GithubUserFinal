package com.developer2t.mygithubuserfinal.adapter

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
import com.developer2t.mygithubuserfinal.database.UserHelper
import com.developer2t.mygithubuserfinal.listener.CustomOnItemClickListener
import com.developer2t.mygithubuserfinal.model.User
import com.developer2t.mygithubuserfinal.view.activity.DetailUser
import kotlinx.android.synthetic.main.list_user_favorite.view.*

class CardUserFavAdapter(val context: Context) :
    RecyclerView.Adapter<CardUserFavAdapter.CardViewViewHolder>() {


    private val mData = ArrayList<User>()
    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<User> {
        return mData
    }

    fun removeItem(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, mData.size)
    }

    override fun onCreateViewHolder(
        viewGrup: ViewGroup,
        viewType: Int
    ): CardViewViewHolder {
        val view = LayoutInflater.from(viewGrup.context)
            .inflate(R.layout.list_user_favorite, viewGrup, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {

            val userHelper = UserHelper(itemView.context)
            userHelper.open()

            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.images)
                    .apply(RequestOptions().override(65, 65))
                    .into(user_images)

                usr_fav.text = user.username

                btn_delete.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val result = userHelper.deleteById(user.id.toString()).toLong()
                            val msg: String

                            if (result > 0) {
                                msg = resources.getString(
                                    R.string.msg_delete_fav_success,
                                    user.username
                                )
                                removeItem(position)
//                                sendUpdate()
                            } else {
                                msg = resources.getString(
                                    R.string.msg_delete_fav_failed,
                                    user.username
                                )
                            }

                            Toast.makeText(
                                context,
                                msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ))

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailUser::class.java)
                    intent.putExtra(DetailUser.EXTRA_USER, user.username)
//                    intent.putExtra(FollowersFragment.EXTRA_USER, users.username)
                    ContextCompat.startActivity(context, intent, null)
                }

            }
        }

    }

//    fun sendUpdate() {
//        val intent = Intent(context, UserFavWidget::class.java)
//        intent.action = UserFavWidget.WIDGET_UPDATE
//        context.sendBroadcast(intent)
//    }
}