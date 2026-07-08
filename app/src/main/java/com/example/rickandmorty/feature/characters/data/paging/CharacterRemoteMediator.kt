package com.example.rickandmorty.feature.characters.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmorty.core.database.CharacterDatabase
import com.example.rickandmorty.core.database.entity.CharacterEntity
import com.example.rickandmorty.core.database.entity.CharacterRemoteKey
import com.example.rickandmorty.core.network.RickAndMortyApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val rickAndMortyApi: RickAndMortyApi,
    private val characterDatabase: CharacterDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            val response = rickAndMortyApi.getCharacters(page = page)
            val characters = response.results
            val endOfPaginationReached = characters.isEmpty()

            characterDatabase.withTransaction {
                // Per user request, we use UPSERT and do NOT clear on refresh
                
                val prevPage = if (page == 1) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1

                val keys = characters.map {
                    CharacterRemoteKey(
                        id = it.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                characterDatabase.dao.insertAllRemoteKeys(keys)
                
                val entities = characters.map {
                    CharacterEntity(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        type = it.type,
                        gender = it.gender,
                        imageUrl = it.image,
                        locationName = it.location.name,
                        originName = it.origin.name
                    )
                }
                characterDatabase.dao.insertAll(entities)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): CharacterRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                characterDatabase.dao.remoteKeyCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): CharacterRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterDatabase.dao.remoteKeyCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                characterDatabase.dao.remoteKeyCharacterId(characterId)
            }
        }
    }
}
