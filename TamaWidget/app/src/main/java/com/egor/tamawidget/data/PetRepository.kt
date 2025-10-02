package com.egor.tamawidget.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import com.egor.tamawidget.domain.PetState

class PetRepository(private val context: Context) {
    val stateFlow: Flow<PetState> = context.petDataStore.data.map { it.toPetState() }

    suspend fun getOnce(): PetState = context.petDataStore.data.map { it.toPetState() }.first()

    suspend fun save(state: PetState) {
        context.petDataStore.edit { p ->
            p[Keys.HUNGER] = state.hunger
            p[Keys.ENERGY] = state.energy
            p[Keys.MOOD] = state.mood
            p[Keys.HYGIENE] = state.hygiene
            p[Keys.HEALTH] = if (state.health.name == "NORMAL") 0 else 1
            p[Keys.AGE_DAYS] = state.ageDays
            p[Keys.IS_SLEEPING] = state.isSleeping
            p[Keys.LAST_UPDATED] = state.lastUpdatedAt
        }
    }
}