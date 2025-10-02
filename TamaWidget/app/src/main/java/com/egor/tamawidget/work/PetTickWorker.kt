package com.egor.tamawidget.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import com.egor.tamawidget.data.PetRepository
import com.egor.tamawidget.domain.tickState
import com.egor.tamawidget.widget.PetWidget

class PetTickWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val repo = PetRepository(applicationContext)
        val now = System.currentTimeMillis()
        val state = repo.stateFlow.first()
        val updated = tickState(state, now)
        if (updated != state) repo.save(updated)
        PetWidget().updateAll(applicationContext)
        return Result.success()
    }

    companion object {
        private const val UNIQUE = "pet_tick_periodic"
        fun schedule(context: Context) {
            val req = PeriodicWorkRequestBuilder<PetTickWorker>(30, TimeUnit.MINUTES)
                .setInitialDelay(30, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE, ExistingPeriodicWorkPolicy.KEEP, req
            )
        }
    }
}