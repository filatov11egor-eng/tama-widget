package com.egor.tamawidget.domain

import kotlin.math.max
import kotlin.math.min

private fun clamp(v: Int) = v.coerceIn(0, 100)

fun tickState(old: PetState, now: Long): PetState {
    if (now <= old.lastUpdatedAt) return old

    val minutes = ((now - old.lastUpdatedAt) / 60000L).toInt()
    if (minutes <= 0) return old.copy(lastUpdatedAt = now)

    var hunger = old.hunger
    var energy = old.energy
    var mood = old.mood
    var hygiene = old.hygiene

    if (old.isSleeping) {
        val ticks15 = minutes / GameRules.TICK_MINUTES
        energy += GameRules.S_ENERGY * ticks15
        hunger -= GameRules.S_HUNGER * ticks15
        mood -= (GameRules.S_MOOD_PER_HOUR * (minutes / 60.0)).toInt()
    } else {
        val ticks15 = minutes / GameRules.TICK_MINUTES
        hunger -= GameRules.D_HUNGER * ticks15
        mood -= GameRules.D_MOOD * ticks15
        hygiene -= GameRules.D_HYGIENE * ticks15
        energy -= GameRules.D_ENERGY * ticks15
    }

    return old.copy(
        hunger = clamp(hunger),
        energy = clamp(energy),
        mood = clamp(mood),
        hygiene = clamp(hygiene),
        lastUpdatedAt = now
    )
}

enum class Action { Feed, Play, Wash, ToggleSleep }

fun applyAction(state: PetState, action: Action, now: Long): PetState {
    val s = tickState(state, now)
    return when (action) {
        Action.Feed -> s.copy(
            hunger = clamp(s.hunger + 30),
            hygiene = clamp(s.hygiene - 5)
        )
        Action.Play -> s.copy(
            mood = clamp(s.mood + 25),
            energy = clamp(s.energy - 10),
            hygiene = clamp(s.hygiene - 5)
        )
        Action.Wash -> s.copy(
            hygiene = clamp(s.hygiene + 40),
            energy = clamp(s.energy - 5)
        )
        Action.ToggleSleep -> s.copy(isSleeping = !s.isSleeping)
    }
}

fun evaluateStatus(s: PetState): String {
    if (s.isSleeping) return "Спит"
    val problems = buildList {
        if (s.hunger < GameRules.WARNING_THRESHOLD) add("Голоден")
        if (s.energy < GameRules.WARNING_THRESHOLD) add("Устал")
        if (s.mood < GameRules.WARNING_THRESHOLD) add("Грустит")
        if (s.hygiene < GameRules.WARNING_THRESHOLD) add("Грязнуля")
    }
    return if (problems.isEmpty()) "В порядке" else problems.joinToString(", ")
}