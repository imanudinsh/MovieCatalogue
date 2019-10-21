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
import com.im.topmovie.model.Movie
import com.im.topmovie.model.Trailer
import com.im.topmovie.utils.CustomToast
import com.im.topmovie.utils.ViewMessages
import com.im.topmovie.utils.values.CategoryEnum
import com.im.topmovie.utils.values.ToastEnum
import com.im.topmovie.viewmodel.MovieDetailViewModel
import java.text.SimpleDateFormat




class MovieDetailActivity : AppCompatActivity(), ViewMessages {


    private lateinit var moviesViewModel: MovieDetailViewModel
    private lateinit var youTubePlayerFragment: YouTubePlayerSupportFragment
    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var trailerKey: String

    companion object{
        const val EXTRA_FILM = "EXTRA_FILM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val item = intent.getParcelableExtra<Movie>(EXTRA_FILM)
        toolbar_title.text= item.title
        iv_poster.z = 5f
        Log.d("DetailActivity","data $item")

        val year = DateFormat.format("yyyy", SimpleDateFormat("yyyy-MM-dd").parse(item.releaseDate))

        tv_tittle.text = item.title
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
        moviesViewModel.onViewAttached(item.id, CategoryEnum.MOVIE.value)
        moviesViewModel.getTrailer().observe(this, getTrailerKey)
        moviesViewModel.messagesEvent.setEventReceiver(this, this)

    }


    private val getTrailerKey = object : Observer<Trailer?> {
        override fun onChanged(trailer: Trailer?) {
            if (trailer != null) {
                trailerKey = trailer.key
                initializeYoutubePlayer()
            }else{
                CustomToast().show(this@MovieDetailActivity,resources.getString(R.string.error_msg_bad_connection), ToastEnum.FAILED.value)
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
