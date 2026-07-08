package com.example.rickandmorty.feature.characters.domain.repository

import androidx.paging.PagingData
import com.example.rickandmorty.feature.characters.domain.model.Character
import com.example.rickandmorty.core.common.Result
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersPaged(): Flow<PagingData<Character>>
    suspend fun getCharacter(id: Int): Result<Character>
}
