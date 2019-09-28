package com.im.layarngaca21.view.main

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.im.layarngaca21.R
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.view.moviedetail.TVShowDetailActivity
import kotlinx.android.synthetic.main.item_row.view.*
import android.support.v4.util.Pair as UtilPair

class TVShowViewAdapter(val activity: Activity) : RecyclerView.Adapter<TVShowViewAdapter.CardViewViewHolder>() {

    private val mData = mutableListOf<TV>()

    fun setData(items: List<TV>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: TV) {
        mData.add(item)
        notifyDataSetChanged()
    }


    fun clearData() {
        mData.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val p = mData[position]
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w185${p.poster}")
            .placeholder(R.drawable.img_placeholder)
            .into(holder.itemView.img_illustartion)
        holder.itemView.tv_tittle.text = p.name
        holder.itemView.tv_number.text = (position+1).toString()
        val rate = (p.rate.toFloat()*10).toInt()
        holder.itemView.tv_score.text = String.format("%s%%", rate)
        holder.itemView.rb_score.rating = rate/20f


        holder.itemView.cardView.setOnClickListener {

            val p1  = UtilPair.create<View, String>(holder.itemView.cardView, "container")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1)

            val intent = Intent(it.context, TVShowDetailActivity::class.java)
            intent.putExtra(TVShowDetailActivity.EXTRA_FILM, p)
            it.context.startActivity(intent, options.toBundle())
        }

    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}