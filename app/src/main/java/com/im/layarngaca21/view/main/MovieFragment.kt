package com.im.layarngaca21.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.im.layarngaca21.R
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.utils.CustomToast
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : Fragment(), ViewMessages{


    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var adapter: MovieViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_movie, container, false)

        activity?.window?.setSharedElementExitTransition(TransitionInflater.from(context).inflateTransition(R.transition.element_transition))

        adapter = MovieViewAdapter(activity!!)
        adapter.notifyDataSetChanged()

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerCardView()
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        moviesViewModel.getMovies().observe(this, getMovie)
        moviesViewModel.messagesEvent.setEventReceiver(this, this)

        moviesViewModel.setMovies()
        progressBar.visibility = View.VISIBLE

    }

    private val getMovie = object : Observer<List<Movie>?> {
        override fun onChanged(listMovie: List<Movie>?) {
            if (listMovie != null) {
                adapter.setData(listMovie)
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                Log.d("MovieFragment","$listMovie")
            }
        }
    }

    private fun showRecyclerCardView() {
        rv_category.layoutManager = LinearLayoutManager(context)
        rv_category.adapter = adapter
    }

    override fun showMessage(message: Int, category: String) {
        context?.let { CustomToast().show(it, resources.getString(message), category) }
        progressBar.visibility = View.GONE
        view_no_data.visibility = if(adapter.itemCount>0) View.GONE else View.VISIBLE
    }


}
