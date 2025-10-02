package com.egor.tamawidget.widget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.action.ActionCallback
import kotlinx.coroutines.flow.first
import com.egor.tamawidget.data.PetRepository
import com.egor.tamawidget.domain.Action
import com.egor.tamawidget.domain.applyAction
import com.egor.tamawidget.widget.PetWidget

class PlayAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: androidx.glance.action.ActionParameters) {
        val repo = PetRepository(context)
        val now = System.currentTimeMillis()
        val current = repo.stateFlow.first()
        val updated = applyAction(current, Action.Play, now)
        repo.save(updated)
        PetWidget().update(context, glanceId)
    }
}