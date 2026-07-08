package com.example.rickandmorty.feature.characters.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmorty.core.database.CharacterDatabase
import com.example.rickandmorty.feature.characters.data.paging.CharacterRemoteMediator
import com.example.rickandmorty.core.network.RickAndMortyApi
import com.example.rickandmorty.feature.characters.domain.model.Character
import com.example.rickandmorty.feature.characters.domain.repository.CharacterRepository
import com.example.rickandmorty.core.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val db: CharacterDatabase
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersPaged(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(api, db),
            pagingSourceFactory = { db.dao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomainModel() }
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return try {
            val localCharacter = db.dao.getCharacterById(id)
            if (localCharacter != null) {
                Result.Success(localCharacter.toDomainModel())
            } else {
                val remoteCharacter = api.getCharacter(id)
                Result.Success(
                    Character(
                        id = remoteCharacter.id,
                        name = remoteCharacter.name,
                        status = remoteCharacter.status,
                        species = remoteCharacter.species,
                        type = remoteCharacter.type,
                        gender = remoteCharacter.gender,
                        imageUrl = remoteCharacter.image,
                        locationName = remoteCharacter.location.name,
                        originName = remoteCharacter.origin.name
                    )
                )
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unexpected error occurred", e)
        }
    }
}
