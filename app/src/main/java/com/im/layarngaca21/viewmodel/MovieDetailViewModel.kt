package com.im.layarngaca21.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
import com.im.layarngaca21.model.Trailer
import com.im.layarngaca21.model.TrailerResponse
import com.im.layarngaca21.utils.LiveMessageEvent
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.utils.values.ResponseCodeEnum
import com.im.layarngaca21.utils.values.ToastEnum


/**
 * Created by Imanudin Sholeh on 09/20/2019.
 */

class MovieDetailViewModel(application: Application) : AndroidViewModel(application)  {

    private val trailer = MutableLiveData<Trailer?>()
    val messagesEvent = LiveMessageEvent<ViewMessages>()
    private var db: AppDatabase? = null
    private lateinit var repository: FavoriteRepository
    private lateinit var favorite: LiveData<List<Favorite>>
    private val app = application


    internal fun onViewAttached(movieId: String, category: String){
        db = AppDatabase.getAppDataBase(app)
        repository = FavoriteRepository(db!!.favoriteDao(), category)
        favorite = repository.getFavorite(movieId)
        setTrailer(movieId, category)
    }
    
    internal fun setTrailer(movieId: String, category: String) {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/$category/$movieId/videos?api_key=${BuildConfig.MOVIE_API_KEY}&language=en-US"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray) {
                try {
                    if(statusCode == ResponseCodeEnum.OK.code){
                        val trailerResponse = Gson().fromJson(String(responseBody), TrailerResponse::class.java)
                        trailer.postValue(trailerResponse.results?.get(0))
                    }else{
                        messagesEvent.sendEvent { showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable) {
                messagesEvent.sendEvent { showMessage(R.string.error_msg_bad_connection, ToastEnum.FAILED.value)}
                Log.d("onFailure", error.message)
            }
        })
    }


    internal fun getTrailer(): LiveData<Trailer?> {
        return trailer
    }

    internal fun insertFavorite(fav: Favorite) {
        repository.insert(fav)
    }

    internal fun deleteFavorite(fav: Favorite) {
        repository.delete(fav)
    }

    internal fun getFavorite(): LiveData<List<Favorite>> {
        return favorite
    }

}
