package com.example.rickandmorty.feature.characters.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.feature.characters.domain.model.Character
import com.example.rickandmorty.feature.characters.domain.usecase.GetCharacterDetailUseCase
import com.example.rickandmorty.core.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailUiState(
    val character: Character? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        val characterId: Int? = savedStateHandle["characterId"]
        if (characterId != null) {
            loadCharacter(characterId)
        } else {
            _uiState.update { it.copy(error = "Unknown Character ID", isLoading = false) }
        }
    }

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getCharacterDetailUseCase(id)) {
                is Result.Success -> {
                    _uiState.update { it.copy(character = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoading = false) }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
