package com.egor.tamawidget.widget

import android.content.Context
import androidx.glance.*
import androidx.glance.Image
import androidx.glance.ImageProvider
import com.egor.tamawidget.R
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.flow.first
import com.egor.tamawidget.data.PetRepository
import com.egor.tamawidget.domain.PetState
import com.egor.tamawidget.domain.evaluateStatus

class PetWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = PetRepository(context)
        val state = repo.stateFlow.first()

        provideContent {
            PetWidgetContent(state)
        }
    }
}

@Composable
fun PetWidgetContent(state: PetState) {
    Column(
        modifier = GlanceModifier.fillMaxSize().padding(16.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        
        // Choose avatar by state
        val avatarRes = when {
            state.isSleeping -> R.drawable.pet_sleep
            state.hunger < 25 || state.energy < 25 || state.mood < 25 || state.hygiene < 25 -> R.drawable.pet_dirty
            else -> R.drawable.pet_normal
        }
        Image(provider = ImageProvider(avatarRes), contentDescription = "Питомец")
        Spacer(8.dp)
    
        Row { Text("🍗 ${state.hunger}"); Spacer(8.dp); Text("⚡ ${state.energy}") }
        Row { Text("😊 ${state.mood}"); Spacer(8.dp); Text("🧼 ${state.hygiene}") }
        Spacer(12.dp)
        Row(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
            androidx.glance.appwidget.widget.Button(text = "Кормить", onClick = actionRunCallback<com.egor.tamawidget.widget.actions.FeedAction>())
            Spacer(8.dp)
            androidx.glance.appwidget.widget.Button(text = if (state.isSleeping) "Разбудить" else "Спать", onClick = actionRunCallback<com.egor.tamawidget.widget.actions.ToggleSleepAction>())
        }
        Spacer(8.dp)
        Row(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
            androidx.glance.appwidget.widget.Button(text = "Играть", onClick = actionRunCallback<com.egor.tamawidget.widget.actions.PlayAction>())
            Spacer(8.dp)
            androidx.glance.appwidget.widget.Button(text = "Мыть", onClick = actionRunCallback<com.egor.tamawidget.widget.actions.WashAction>())
        }
        Spacer(8.dp)
        Text(evaluateStatus(state))
    }
}

class PetWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PetWidget()
}