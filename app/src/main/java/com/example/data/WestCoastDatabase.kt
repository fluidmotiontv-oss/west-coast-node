package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Memory::class,
        ArtistLedger::class,
        Quest::class,
        UserSession::class,
        MusicMetadata::class,
        ARStoryConfig::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WestCoastDatabase : RoomDatabase() {
    abstract fun studioDao(): StudioDao
    abstract fun sessionDao(): UserSessionDao
    abstract fun musicDao(): MusicMetadataDao
    abstract fun arConfigDao(): ARStoryConfigDao
}
