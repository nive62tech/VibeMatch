package com.vibematch.data.model

data class Person(
    val id: String,
    val displayName: String,
    val age: Int? = null
)

data class Preference(
    val personId: String,
    val vibe: List<String>,
    val setting: String,        // "indoor" | "outdoor" | "either"
    val energyLevel: Int,       // 1-5
    val budgetRange: String,    // "low" | "medium" | "high"
    val cuisineTags: List<String> = emptyList(),
    val dislikeTags: List<String> = emptyList()
)

data class Candidate(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val vibeTags: List<String>,
    val crowdLevel: String?,
    val priceLevel: String?,
    val setting: String? = "either",
    val energyLevel: Int? = 3,
    val minAge: Int? = null
)

data class PlanningSession(
    val id: String,
    val groupId: String,
    val occasionMode: String,
    val status: String   // "collecting" | "revealed" | "closed"
)

data class Vote(
    val id: String,
    val sessionId: String,
    val personId: String,
    val candidateId: String,
    val score: Int
)

data class OutingHistory(
    val id: String,
    val groupId: String,
    val candidateName: String,
    val visitedAt: Long   // epoch millis
)