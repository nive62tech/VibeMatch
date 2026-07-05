package com.vibematch.data.local

import android.content.Context
import org.json.JSONArray

object SeedLoader {
    fun loadSeedCandidates(context: Context): List<CandidateEntity> {
        val json = context.assets.open("seed_venues.json")
            .bufferedReader().use { it.readText() }
        val array = JSONArray(json)
        val list = mutableListOf<CandidateEntity>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val vibeTags = mutableListOf<String>()
            val vibeArray = obj.optJSONArray("vibe_tags")
            if (vibeArray != null) {
                for (j in 0 until vibeArray.length()) vibeTags.add(vibeArray.getString(j))
            }

            list.add(
                CandidateEntity(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    category = obj.getString("category"),
                    description = obj.getString("description"),
                    vibeTags = vibeTags,
                    crowdLevel = obj.optString("crowd_level", null),
                    priceLevel = obj.optString("price_level", null),
                    setting = obj.optString("setting", "either"),
                    energyLevel = if (obj.has("energy_level")) obj.getInt("energy_level") else 3,
                    minAge = if (obj.has("min_age")) obj.getInt("min_age") else null
                )
            )
        }
        return list
    }
}