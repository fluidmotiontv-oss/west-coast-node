package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory")
data class Memory(
    @PrimaryKey val key: String,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "artist_ledgers")
data class ArtistLedger(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artistName: String,
    val harmonyNotes: Long,
    val shavings: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val rewardNotes: Int,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_sessions")
data class UserSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artistName: String,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val status: String = "ACTIVE"
)

@Entity(tableName = "music_metadata")
data class MusicMetadata(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val bpm: Int,
    val genre: String,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "ar_story_configs")
data class ARStoryConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val overlayType: String,
    val opacity: Float = 0.2f,
    val timestamp: Long = System.currentTimeMillis()
)
