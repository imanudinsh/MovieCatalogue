package com.im.layarngaca21.ui.main

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.im.layarngaca21.R
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.ui.moviedetail.MovieDetailActivity
import kotlinx.android.synthetic.main.item_row.view.*
import androidx.core.util.Pair as UtilPair

class MovieViewAdapter(val activity: Activity, val favListener: (Movie, ImageView, Boolean) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<MovieViewAdapter.CardViewViewHolder>() {

    private val mData = mutableListOf<Movie>()
    private val listFavorites = mutableListOf<Favorite>()

    fun setData(items: List<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun setFavorites(items: List<Favorite>) {
        listFavorites.clear()
        listFavorites.addAll(items)
        notifyDataSetChanged()
    }

    fun addFavorite(item: Favorite) {
        listFavorites.add(item)
    }

    fun removeFavorite(item: Favorite) {
        listFavorites.remove(item)
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
        if(listFavorites.any { it.id == p.id }) holder.itemView.iv_heart.imageTintList = activity.getColorStateList(R.color.red)

        holder.itemView.cardView.setOnClickListener {

            val p1 = UtilPair.create<View, String>(holder.itemView.cardView, "container")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1)

            val intent = Intent(it.context, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.EXTRA_FILM, p)
            it.context.startActivity(intent, options.toBundle())
        }

        holder.itemView.btn_favorite.setOnClickListener {
            favListener(p, holder.itemView.iv_heart,listFavorites.any { it.id == p.id })
        }

    }

    inner class CardViewViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {


    }
}