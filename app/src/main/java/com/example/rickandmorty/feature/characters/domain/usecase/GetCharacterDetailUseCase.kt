package com.example.rickandmorty.feature.characters.domain.usecase

import com.example.rickandmorty.feature.characters.domain.model.Character
import com.example.rickandmorty.feature.characters.domain.repository.CharacterRepository
import com.example.rickandmorty.core.common.Result
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Result<Character> {
        return repository.getCharacter(id)
    }
}
