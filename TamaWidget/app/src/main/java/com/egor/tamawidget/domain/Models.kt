package com.egor.tamawidget.domain

enum class HealthStatus { NORMAL, SICK }

data class PetState(
    val hunger: Int = 100,
    val energy: Int = 100,
    val mood: Int = 100,
    val hygiene: Int = 100,
    val health: HealthStatus = HealthStatus.NORMAL,
    val ageDays: Int = 0,
    val isSleeping: Boolean = false,
    val lastUpdatedAt: Long = System.currentTimeMillis()
)

data class GameLogItem(
    val ts: Long,
    val type: String,
    val delta: Map<String, Int> = emptyMap()
)