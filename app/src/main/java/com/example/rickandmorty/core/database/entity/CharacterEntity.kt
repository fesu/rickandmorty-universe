package com.example.rickandmorty.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.feature.characters.domain.model.Character

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val imageUrl: String,
    val locationName: String,
    val originName: String
) {
    fun toDomainModel(): Character {
        return Character(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            imageUrl = imageUrl,
            locationName = locationName,
            originName = originName
        )
    }
}
