package com.egor.tamawidget.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import com.egor.tamawidget.domain.HealthStatus
import com.egor.tamawidget.domain.PetState

val Context.petDataStore by preferencesDataStore("pet_state")

object Keys {
    val HUNGER = intPreferencesKey("hunger")
    val ENERGY = intPreferencesKey("energy")
    val MOOD = intPreferencesKey("mood")
    val HYGIENE = intPreferencesKey("hygiene")
    val HEALTH = intPreferencesKey("health")
    val AGE_DAYS = intPreferencesKey("ageDays")
    val IS_SLEEPING = booleanPreferencesKey("isSleeping")
    val LAST_UPDATED = longPreferencesKey("lastUpdated")
}

fun Preferences.toPetState(): PetState = PetState(
    hunger = this[Keys.HUNGER] ?: 100,
    energy = this[Keys.ENERGY] ?: 100,
    mood = this[Keys.MOOD] ?: 100,
    hygiene = this[Keys.HYGIENE] ?: 100,
    health = if ((this[Keys.HEALTH] ?: 0) == 0) HealthStatus.NORMAL else HealthStatus.SICK,
    ageDays = this[Keys.AGE_DAYS] ?: 0,
    isSleeping = this[Keys.IS_SLEEPING] ?: false,
    lastUpdatedAt = this[Keys.LAST_UPDATED] ?: System.currentTimeMillis()
)