package com.example.rickandmorty.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys")
data class CharacterRemoteKey(
    @PrimaryKey val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)
