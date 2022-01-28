package com.example.smb2b

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    private var requestCode = 0

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        Log.i("widget-app", "Pierwszy widget dodany.")
    }

    override fun onDisabled(context: Context) {
        Log.i("widget-app", "Ostatni widget usunięty.")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Toast.makeText(context, "Intent", Toast.LENGTH_SHORT).show()
        if (intent?.action.equals("com.example.smb2b.Action1"))
            Toast.makeText(context, "Action1", Toast.LENGTH_SHORT).show()
    }
}
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)

    val wwwIntent = Intent(Intent.ACTION_VIEW)
    wwwIntent.data = Uri.parse("https://www.pja.edu.pl")
    val pendingwwwIntent = PendingIntent.getActivity(
        context,
        1,
        wwwIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.btWWW, pendingwwwIntent)

    val toastIntent = Intent("com.example.smb2b.Action1")
    toastIntent.component = ComponentName(context, NewAppWidget::class.java)
    val pendingtoastIntent = PendingIntent.getBroadcast(
        context,
        1,
        toastIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.btActionPlay, pendingtoastIntent)


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}