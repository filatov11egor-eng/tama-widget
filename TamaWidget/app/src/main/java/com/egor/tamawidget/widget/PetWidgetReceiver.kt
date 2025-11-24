package com.egor.tamawidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.RemoteViews
import com.egor.tamawidget.R

class PetWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId, isTextShown(context, appWidgetId))
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TOGGLE) {
            val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val newState = !isTextShown(context, widgetId)
                saveState(context, widgetId, newState)
                val manager = AppWidgetManager.getInstance(context)
                updateWidget(context, manager, widgetId, newState)
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val prefs = preferences(context)
        appWidgetIds.forEach { appWidgetId ->
            prefs.edit().remove(stateKey(appWidgetId)).apply()
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        isTextVisible: Boolean
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_toggle)

        views.setViewVisibility(R.id.toggle_button, if (isTextVisible) View.GONE else View.VISIBLE)
        views.setViewVisibility(R.id.toggle_text, if (isTextVisible) View.VISIBLE else View.GONE)

        val toggleIntent = Intent(context, PetWidgetReceiver::class.java).apply {
            action = ACTION_TOGGLE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }

        val togglePendingIntent = PendingIntent.getBroadcast(
            context,
            appWidgetId,
            toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(R.id.toggle_button, togglePendingIntent)
        views.setOnClickPendingIntent(R.id.toggle_text, togglePendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun preferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun isTextShown(context: Context, appWidgetId: Int): Boolean =
        preferences(context).getBoolean(stateKey(appWidgetId), false)

    private fun saveState(context: Context, appWidgetId: Int, isTextVisible: Boolean) {
        preferences(context).edit().putBoolean(stateKey(appWidgetId), isTextVisible).apply()
    }

    private fun stateKey(appWidgetId: Int) = "${STATE_PREFIX}$appWidgetId"

    companion object {
        private const val ACTION_TOGGLE = "com.egor.tamawidget.widget.action.TOGGLE"
        private const val PREFS_NAME = "com.egor.tamawidget.widget.PREFERENCES"
        private const val STATE_PREFIX = "state_"
    }
}
