package com.vibematch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PersonEntity::class,
        PreferenceEntity::class,
        CandidateEntity::class,
        PlanningSessionEntity::class,
        VoteEntity::class,
        OutingHistoryEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class VibeMatchDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun preferenceDao(): PreferenceDao
    abstract fun candidateDao(): CandidateDao
    abstract fun sessionDao(): SessionDao
    abstract fun voteDao(): VoteDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: VibeMatchDatabase? = null

        fun getInstance(context: Context): VibeMatchDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    VibeMatchDatabase::class.java,
                    "vibematch.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}