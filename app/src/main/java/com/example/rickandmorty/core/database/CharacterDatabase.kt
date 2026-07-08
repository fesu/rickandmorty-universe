package com.example.rickandmorty.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmorty.core.database.entity.CharacterEntity
import com.example.rickandmorty.core.database.entity.CharacterRemoteKey

@Database(
    entities = [CharacterEntity::class, CharacterRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class CharacterDatabase : RoomDatabase() {
    abstract val dao: CharacterDao
}
