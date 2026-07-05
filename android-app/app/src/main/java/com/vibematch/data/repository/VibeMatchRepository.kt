package com.vibematch.data.repository

import com.vibematch.data.local.*

class VibeMatchRepository(private val db: VibeMatchDatabase) {

    suspend fun addPerson(person: PersonEntity) = db.personDao().insert(person)
    suspend fun getAllPeople() = db.personDao().getAll()

    suspend fun savePreference(pref: PreferenceEntity) = db.preferenceDao().insert(pref)
    suspend fun getPreference(personId: String) = db.preferenceDao().getByPerson(personId)

    suspend fun getAllCandidates() = db.candidateDao().getAll()
    suspend fun candidateCount() = db.candidateDao().count()
    suspend fun seedCandidatesIfEmpty(candidates: List<CandidateEntity>) {
        if (db.candidateDao().count() == 0) {
            db.candidateDao().insertAll(candidates)
        }
    }

    suspend fun createSession(session: PlanningSessionEntity) = db.sessionDao().insert(session)
    suspend fun getSession(id: String) = db.sessionDao().getById(id)

    suspend fun castVote(vote: VoteEntity) = db.voteDao().insert(vote)
    suspend fun getVotesForSession(sessionId: String) = db.voteDao().getBySession(sessionId)

    suspend fun logHistory(entry: OutingHistoryEntity) = db.historyDao().insert(entry)
    suspend fun getHistory(groupId: String) = db.historyDao().getByGroup(groupId)
}