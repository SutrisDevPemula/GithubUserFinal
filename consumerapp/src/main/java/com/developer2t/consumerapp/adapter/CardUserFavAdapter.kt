package com.developer2t.consumerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.developer2t.consumerapp.R
import com.developer2t.consumerapp.model.User
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

            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.images)
                    .apply(RequestOptions().override(65, 65))
                    .into(user_images)

                usr_fav.text = user.username

            }
        }

    }

}