package com.example.rickandmorty.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object CharacterListRoute

@Serializable
data class CharacterDetailRoute(val characterId: Int)

@Serializable
object EpisodesRoute

@Serializable
object LocationsRoute
