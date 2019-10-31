package com.im.topmovie.ui.main

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.im.topmovie.R
import com.im.topmovie.model.Movie
import com.im.topmovie.ui.moviedetail.MovieDetailActivity
import kotlinx.android.synthetic.main.item_row.view.*
import androidx.core.util.Pair as UtilPair

class MovieViewAdapter(val activity: Activity) : androidx.recyclerview.widget.RecyclerView.Adapter<MovieViewAdapter.CardViewViewHolder>() {

    private val mData = mutableListOf<Movie>()

    fun setData(items: List<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }



    fun addItem(item: Movie) {
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
        holder.setIsRecyclable(false)
        val p = mData[position]
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w185${p.poster}")
            .placeholder(R.drawable.img_placeholder)
            .into(holder.itemView.img_illustartion)
        holder.itemView.tv_tittle.text = p.title
        holder.itemView.tv_number.text = (position+1).toString()
        val rate = (p.rate.toFloat()*10).toInt()
        holder.itemView.tv_score.text = String.format("%s%%", rate)
        holder.itemView.rb_score.rating = rate/20f

        holder.itemView.cardView.setOnClickListener {

            val p1 = UtilPair.create<View, String>(holder.itemView.cardView, "container")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1)

            val intent = Intent(it.context, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.EXTRA_FILM, p)
            it.context.startActivity(intent, options.toBundle())
        }


    }

    inner class CardViewViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {


    }
}