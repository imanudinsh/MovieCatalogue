package com.im.layarngaca21.utils

import android.content.Intent
import android.widget.RemoteViewsService
import com.im.layarngaca21.view.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory{
        return StackRemoteViewsFactory(this.applicationContext)
    }

}
