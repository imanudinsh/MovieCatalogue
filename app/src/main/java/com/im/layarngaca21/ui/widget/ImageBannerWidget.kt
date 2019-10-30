package com.im.layarngaca21.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

import com.im.layarngaca21.R
import com.im.layarngaca21.model.Movie
import com.im.layarngaca21.model.TV
import com.im.layarngaca21.utils.StackWidgetService
import com.im.layarngaca21.utils.values.CategoryEnum
import com.im.layarngaca21.ui.moviedetail.MovieDetailActivity
import com.im.layarngaca21.ui.moviedetail.TVShowDetailActivity


class ImageBannerWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent?.action !=null){
            if(intent.action == TOAST_ACTION){

                val idMovie = intent.getStringExtra("id")
                val titleMovie = intent.getStringExtra("title")
                val dateMovie = intent.getStringExtra("date")
                val rateMovie = intent.getStringExtra("rate")
                val synopsisMovie = intent.getStringExtra("synopsis")
                val posterMovie = intent.getStringExtra("poster")
                val categoryMovie = intent.getStringExtra("category")
                        if(categoryMovie == CategoryEnum.MOVIE.value){
                            val movie = Movie(id = idMovie, rate = rateMovie, poster = posterMovie, synopsis = synopsisMovie, title = titleMovie, releaseDate = dateMovie)
                            val intent = Intent(context, MovieDetailActivity::class.java)
                            intent.putExtra(MovieDetailActivity.EXTRA_FILM, movie)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context?.startActivity(intent)
                        }else{
                            val movie = TV(id = idMovie, rate = rateMovie, poster = posterMovie, synopsis = synopsisMovie, name = titleMovie, firsAirDate = dateMovie)
                            val intent = Intent(context, TVShowDetailActivity::class.java)
                            intent.putExtra(TVShowDetailActivity.EXTRA_FILM, movie)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context?.startActivity(intent)

                        }


            }
        }


    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        private const val TOAST_ACTION = "com.im.layarngaca21.TOAST_ACTION"
        const val EXTRA_ITEM = "com.im.layarngaca21.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)
            val toastIntent = Intent(context, ImageBannerWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

