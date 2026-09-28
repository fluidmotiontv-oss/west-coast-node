package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {
    @Query("SELECT * FROM user_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<UserSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: UserSession)
}

@Dao
interface MusicMetadataDao {
    @Query("SELECT * FROM music_metadata ORDER BY timestamp DESC")
    fun getAllMusic(): Flow<List<MusicMetadata>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicMetadata)
}

@Dao
interface ARStoryConfigDao {
    @Query("SELECT * FROM ar_story_configs ORDER BY timestamp DESC")
    fun getAllARConfigs(): Flow<List<ARStoryConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertARConfig(config: ARStoryConfig)
}

@Dao
interface StudioDao {
    @Query("SELECT * FROM memory")
    fun getAllMemory(): Flow<List<Memory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: Memory)

    @Query("SELECT * FROM artist_ledgers ORDER BY timestamp DESC")
    fun getAllLedgers(): Flow<List<ArtistLedger>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLedger(ledger: ArtistLedger)

    @Query("SELECT * FROM quests ORDER BY timestamp DESC")
    fun getAllQuests(): Flow<List<Quest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: Quest)

    @Update
    suspend fun updateQuest(quest: Quest)
}
