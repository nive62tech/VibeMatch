package com.vibematch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val age: Int?
)

@Entity(tableName = "preferences")
@TypeConverters(Converters::class)
data class PreferenceEntity(
    @PrimaryKey val personId: String,
    val vibe: List<String>,
    val setting: String,
    val energyLevel: Int,
    val budgetRange: String,
    val cuisineTags: List<String>,
    val dislikeTags: List<String>
)

@Entity(tableName = "candidates")
@TypeConverters(Converters::class)
data class CandidateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val vibeTags: List<String>,
    val crowdLevel: String?,
    val priceLevel: String?,
    val setting: String?,
    val energyLevel: Int?,
    val minAge: Int?
)

@Entity(tableName = "sessions")
data class PlanningSessionEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val occasionMode: String,
    val status: String
)

@Entity(tableName = "votes")
data class VoteEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val personId: String,
    val candidateId: String,
    val score: Int
)

@Entity(tableName = "history")
data class OutingHistoryEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val candidateName: String,
    val visitedAt: Long
)