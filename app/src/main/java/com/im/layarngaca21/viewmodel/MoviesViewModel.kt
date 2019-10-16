package com.im.layarngaca21.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.im.layarngaca21.BuildConfig
import com.im.layarngaca21.R
import com.im.layarngaca21.database.AppDatabase
import com.im.layarngaca21.database.entity.Favorite
import com.im.layarngaca21.database.repository.FavoriteRepository

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.model.MovieResponse
import com.im.layarngaca21.utils.LiveMessageEvent
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.utils.values.ResponseCodeEnum
import com.im.layarngaca21.utils.values.ToastEnum


class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val listMovies:MutableLiveData<List<Movie>> = MutableLiveData()
    val messagesEvent = LiveMessageEvent<ViewMessages>()
    private var db: AppDatabase? = null
    private lateinit var repository: FavoriteRepository
    private lateinit var allFavorite: LiveData<List<Favorite>>
    private val app = application


    internal fun onViewAttached(){
        db = AppDatabase.getAppDataBase(app)
        repository = FavoriteRepository(db!!.favoriteDao(), CategoryEnum.MOVIE.value)
        allFavorite = repository.getAllFavorites()
    }
    internal fun setMovies() {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US"
            client.get(url, object : AsyncHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray) {
                        if(statusCode==ResponseCodeEnum.OK.code){
                            val movieResponse = Gson().fromJson(String(responseBody), MovieResponse::class.java)
                            listMovies.postValue(movieResponse.results)
                        }else{
                            messagesEvent.sendEvent {
                                Log.d("MovieViewModel",responseBody.toString())
                                showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                        }


                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                    Log.d("onFailure", error.message)
                    messagesEvent.sendEvent { showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                }
            })
    }

    internal fun getMovies(): LiveData<List<Movie>?> {
        return listMovies
    }

    internal fun insertFavorite(fav: Favorite) {
        repository.insert(fav)
    }

    internal fun deleteFavorite(fav: Favorite) {
        repository.delete(fav)
    }

    internal fun getAllFavorites(): LiveData<List<Favorite>> {
        return allFavorite
    }

}
