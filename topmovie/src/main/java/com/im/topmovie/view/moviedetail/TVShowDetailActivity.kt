package com.im.topmovie.view.moviedetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import android.text.format.DateFormat
import com.im.topmovie.BuildConfig
import com.im.topmovie.R
import com.im.topmovie.model.TV
import com.im.topmovie.model.Trailer
import com.im.topmovie.utils.CustomToast
import com.im.topmovie.utils.ViewMessages
import com.im.topmovie.utils.values.CategoryEnum
import com.im.topmovie.viewmodel.MovieDetailViewModel
import java.text.SimpleDateFormat


class TVShowDetailActivity : AppCompatActivity() , ViewMessages {


    private lateinit var moviesViewModel: MovieDetailViewModel
    private lateinit var youTubePlayerFragment: YouTubePlayerSupportFragment
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var trailerKey: String
    private var isFavorite: Boolean = false

    companion object{
        const val EXTRA_FILM = "EXTRA_FILM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val item = intent.getParcelableExtra<TV>(EXTRA_FILM)
        toolbar_title.text= item.name
        iv_poster.z = 5f

        val year = DateFormat.format("yyyy", SimpleDateFormat("yyyy-MM-dd").parse(item.firsAirDate))
        tv_tittle.text = item.name
        tv_year.text = year
        tv_synopsis.text = item.synopsis
        val rate = (item.rate.toFloat()*10).toInt()
        tv_score.text = String.format("%s%%", rate)
        val score = rate/20f
        rb_score.rating = score
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w185${item.poster}")
            .placeholder(R.drawable.img_placeholder)
            .into(iv_poster)

        moviesViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
        moviesViewModel.onViewAttached(item.id, CategoryEnum.TV.value)
        moviesViewModel.getTrailer().observe(this, getTrailerKey)
        moviesViewModel.messagesEvent.setEventReceiver(this, this)
        moviesViewModel.setTrailer(item.id, CategoryEnum.TV.value)

    }

    private val getTrailerKey = object : Observer<Trailer?> {
        override fun onChanged(trailer: Trailer?) {
            Log.d("DetailMovieActivity","trailer $trailer")
            if (trailer != null) {
                trailerKey = trailer.key
                initializeYoutubePlayer()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initializeYoutubePlayer() {

        youTubePlayerFragment = supportFragmentManager.findFragmentById(R.id.vv_trailer) as YouTubePlayerSupportFragment
        youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider, player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    youTubePlayer = player
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    youTubePlayer.cueVideo(trailerKey)
                    youTubePlayer.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
                        override fun onPlaying() {
                            iv_poster.z = 0f
                        }

                        override fun onPaused() {
                            iv_poster.z = 5f
                        }

                        override fun onStopped() {
                            iv_poster.z = 5f
                        }

                        override fun onBuffering(b: Boolean) {
                        }

                        override fun onSeekTo(i: Int) {
                        }
                    })
                }
                Log.e("DetailActivity", "Youtube Player View initialization success")
            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {
                Log.e("DetailActivity", "Youtube Player View initialization failed")
            }
        })
    }

    override fun showMessage(message: Int, category: String) {
        CustomToast().show(this, resources.getString(message), category)
    }
}
