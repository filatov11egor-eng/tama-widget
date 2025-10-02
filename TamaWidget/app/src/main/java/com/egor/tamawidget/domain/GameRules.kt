package com.egor.tamawidget.domain

object GameRules {
    const val TICK_MINUTES = 15

    // Base degradation per 15 minutes (awake)
    const val D_HUNGER = 4
    const val D_MOOD = 3
    const val D_HYGIENE = 2
    const val D_ENERGY = 1

    // Sleep per 15 minutes
    const val S_ENERGY = 6
    const val S_HUNGER = 2
    const val S_MOOD_PER_HOUR = 1

    const val WARNING_THRESHOLD = 25
}