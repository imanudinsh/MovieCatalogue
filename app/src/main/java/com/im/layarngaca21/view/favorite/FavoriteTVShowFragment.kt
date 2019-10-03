package com.im.layarngaca21.view.favorite

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.im.layarngaca21.R
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.view.main.TVShowViewAdapter
import com.im.layarngaca21.viewmodel.TVShowsViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*


class FavoriteTVShowFragment : Fragment(), ViewMessages {


    private lateinit  var tvShowsViewModel: TVShowsViewModel
    private lateinit var adapter: TVShowViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_tv_show, container, false)

        activity?.window?.setSharedElementExitTransition(TransitionInflater.from(context).inflateTransition(R.transition.element_transition))

        adapter = TVShowViewAdapter(activity!!, favListener = {movie, ivHeart, isFavorite ->
            val fav = Favorite(id= movie.id, title = movie.name, date = movie.firsAirDate, rate = movie.rate, synopsis = movie.synopsis, poster = movie.poster, category = CategoryEnum.TV.value)
            if(isFavorite){
                tvShowsViewModel.deleteFavorite(fav)
                ivHeart.setImageDrawable(context?.getDrawable(R.drawable.ic_heart))
                ivHeart.imageTintList = context?.getColorStateList(R.color.grey)
                adapter.removeFavorite(fav)
            }else{
                tvShowsViewModel.insertFavorite(fav)
                adapter.addFavorite(fav)
                context?.let {
                    val ivAnimation = AnimatedVectorDrawableCompat.create(it, R.drawable.ic_heart_anim)
                    ivHeart.setImageDrawable(ivAnimation)
                    ivAnimation?.start()
                }

            }
        })
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerCardView()

        tvShowsViewModel = ViewModelProviders.of(this).get(TVShowsViewModel::class.java)
        tvShowsViewModel.onViewAttached()
        tvShowsViewModel.getAllFavorites().observe(this, getFavorite)
        tvShowsViewModel.messagesEvent.setEventReceiver(this, this)

    }


    private val getFavorite = object : Observer<List<Favorite>?> {
        override fun onChanged(listFav: List<Favorite>?) {
            if (listFav != null) {
                adapter.setFavorites(listFav)
                val listTV: MutableList<TV> = mutableListOf()
                listFav.forEach {
                    listTV.add(TV(id= it.id, name = it.title, firsAirDate = it.date, rate = it.rate, synopsis = it.synopsis, poster = it.poster))
                }
                adapter.setData(listTV)
            }
        }
    }


    private fun showRecyclerCardView() {
        rv_category.layoutManager = LinearLayoutManager(context)
        rv_category.adapter = adapter
    }


    override fun showMessage(message: Int, category: String) {
        view_no_data.visibility = if(adapter.itemCount>0) View.GONE else View.VISIBLE
    }

}
