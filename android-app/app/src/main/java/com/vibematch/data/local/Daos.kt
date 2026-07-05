package com.vibematch.data.local

import androidx.room.*

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonEntity)

    @Query("SELECT * FROM people")
    suspend fun getAll(): List<PersonEntity>
}

@Dao
interface PreferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preference: PreferenceEntity)

    @Query("SELECT * FROM preferences WHERE personId = :personId")
    suspend fun getByPerson(personId: String): PreferenceEntity?
}

@Dao
interface CandidateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(candidates: List<CandidateEntity>)

    @Query("SELECT * FROM candidates")
    suspend fun getAll(): List<CandidateEntity>

    @Query("SELECT COUNT(*) FROM candidates")
    suspend fun count(): Int
}

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: PlanningSessionEntity)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getById(id: String): PlanningSessionEntity?
}

@Dao
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vote: VoteEntity)

    @Query("SELECT * FROM votes WHERE sessionId = :sessionId")
    suspend fun getBySession(sessionId: String): List<VoteEntity>
}

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(entry: OutingHistoryEntity)

    @Query("SELECT * FROM history WHERE groupId = :groupId ORDER BY visitedAt DESC")
    suspend fun getByGroup(groupId: String): List<OutingHistoryEntity>
}