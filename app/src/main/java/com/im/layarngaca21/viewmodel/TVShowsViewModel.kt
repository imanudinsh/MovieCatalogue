package com.im.layarngaca21.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.gson.Gson
import com.im.layarngaca21.BuildConfig
import com.im.layarngaca21.R

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.model.TVResponse
import com.im.layarngaca21.utils.LiveMessageEvent
import com.im.layarngaca21.utils.ViewMessages
import com.im.layarngaca21.utils.values.ResponseCodeEnum
import com.im.layarngaca21.utils.values.ToastEnum


/**
 * Created by Imanudin Sholeh on 09/20/2019.
 */

class TVShowsViewModel : ViewModel() {

    private val listTv = MutableLiveData<List<TV>?>()
    val messagesEvent = LiveMessageEvent<ViewMessages>()

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
}
