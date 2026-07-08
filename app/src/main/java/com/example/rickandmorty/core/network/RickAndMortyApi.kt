package com.example.rickandmorty.core.network

import com.example.rickandmorty.feature.characters.data.remote.dto.CharacterDto
import com.example.rickandmorty.feature.characters.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): CharacterDto

}
