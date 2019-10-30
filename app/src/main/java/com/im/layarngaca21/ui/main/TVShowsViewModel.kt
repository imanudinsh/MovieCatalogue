package com.im.layarngaca21.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.model.TVResponse
import com.im.layarngaca21.utils.LiveMessageEvent
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.utils.values.ResponseCodeEnum
import com.im.layarngaca21.utils.values.ToastEnum


/**
 * Created by Imanudin Sholeh on 09/20/2019.
 */

class TVShowsViewModel(application: Application) : AndroidViewModel(application)  {

    private val listTv = MutableLiveData<List<TV>?>()
    val messagesEvent = LiveMessageEvent<ViewMessages>()
    private var db: AppDatabase? = null
    private lateinit var repository: FavoriteRepository
    private lateinit var allFavorite: LiveData<List<Favorite>>
    private val app = application


    internal fun onViewAttached(){
        db = AppDatabase.getAppDataBase(app)
        repository = FavoriteRepository(db!!.favoriteDao(), CategoryEnum.TV.value)
        allFavorite = repository.getAllFavorites()
    }
    internal fun setTVShows() {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US"

        try {
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray) {
                        if(statusCode== ResponseCodeEnum.OK.code) {
                            val tvResponse = Gson().fromJson(String(responseBody), TVResponse::class.java)
                            listTv.postValue(tvResponse.results)
                        }else{
                            messagesEvent.sendEvent { showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                        }

                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                    messagesEvent.sendEvent { showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                    Log.d("onFailure", error.message)
                }
            })
        } catch (e: Exception) {
            Log.d("Exception", e.message)
        }
    }

    internal fun getTVShows(): LiveData<List<TV>?> {
        return listTv
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
