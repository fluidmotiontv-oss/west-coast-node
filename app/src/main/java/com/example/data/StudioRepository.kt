package com.example.data

import kotlinx.coroutines.flow.Flow

class StudioRepository(
    private val studioDao: StudioDao,
    private val sessionDao: UserSessionDao,
    private val musicDao: MusicMetadataDao,
    private val arConfigDao: ARStoryConfigDao
) {
    val memory: Flow<List<Memory>> = studioDao.getAllMemory()
    val ledgers: Flow<List<ArtistLedger>> = studioDao.getAllLedgers()
    val quests: Flow<List<Quest>> = studioDao.getAllQuests()
    val sessions: Flow<List<UserSession>> = sessionDao.getAllSessions()
    val music: Flow<List<MusicMetadata>> = musicDao.getAllMusic()
    val arConfigs: Flow<List<ARStoryConfig>> = arConfigDao.getAllARConfigs()

    suspend fun saveMemory(key: String, value: String) {
        studioDao.insertMemory(Memory(key, value))
    }

    suspend fun addLedger(artistName: String, harmonyNotes: Long, shavings: Long) {
        studioDao.insertLedger(ArtistLedger(artistName = artistName, harmonyNotes = harmonyNotes, shavings = shavings))
    }

    suspend fun addQuest(title: String, description: String, rewardNotes: Int) {
        studioDao.insertQuest(Quest(title = title, description = description, rewardNotes = rewardNotes))
    }

    suspend fun completeQuest(quest: Quest) {
        studioDao.updateQuest(quest.copy(isCompleted = true))
    }

    suspend fun startSession(artistName: String) {
        sessionDao.insertSession(UserSession(artistName = artistName))
    }

    suspend fun addMusic(title: String, bpm: Int, genre: String, duration: Int) {
        musicDao.insertMusic(MusicMetadata(title = title, bpm = bpm, genre = genre, durationSeconds = duration))
    }

    suspend fun addARConfig(title: String, overlayType: String) {
        arConfigDao.insertARConfig(ARStoryConfig(title = title, overlayType = overlayType))
    }
}
