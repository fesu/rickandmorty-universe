package com.example.rickandmorty.core.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmorty.core.database.entity.CharacterEntity
import com.example.rickandmorty.core.database.entity.CharacterRemoteKey

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("DELETE FROM characters")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(remoteKeys: List<CharacterRemoteKey>)

    @Query("SELECT * FROM character_remote_keys WHERE id = :id")
    suspend fun remoteKeyCharacterId(id: Int): CharacterRemoteKey?

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearRemoteKeys()
}
