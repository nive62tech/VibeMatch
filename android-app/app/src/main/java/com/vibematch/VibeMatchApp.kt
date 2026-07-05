package com.vibematch

import android.app.Application
import com.vibematch.data.local.SeedLoader
import com.vibematch.data.local.VibeMatchDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VibeMatchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = VibeMatchDatabase.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            val seedData = SeedLoader.loadSeedCandidates(applicationContext)
            if (db.candidateDao().count() == 0) {
                db.candidateDao().insertAll(seedData)
            }
            android.util.Log.d("VibeMatch", "Seeded ${seedData.size} candidates")
        }
    }
}