package com.im.topmovie.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.im.topmovie.BuildConfig
import com.im.topmovie.R
import com.im.topmovie.model.Trailer
import com.im.topmovie.model.TrailerResponse
import com.im.topmovie.utils.LiveMessageEvent
import com.im.topmovie.utils.ViewMessages
import com.im.topmovie.utils.values.ResponseCodeEnum
import com.im.topmovie.utils.values.ToastEnum
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header

class MovieDetailViewModel(application: Application) : AndroidViewModel(application)  {

    private val trailer = MutableLiveData<Trailer?>()
    val messagesEvent = LiveMessageEvent<ViewMessages>()


    internal fun onViewAttached(movieId: String, category: String){
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


}
