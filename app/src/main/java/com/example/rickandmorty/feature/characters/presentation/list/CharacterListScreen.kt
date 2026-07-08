package com.example.rickandmorty.feature.characters.presentation.list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.rickandmorty.feature.characters.presentation.component.CharacterItem
import com.example.rickandmorty.feature.characters.presentation.component.CharacterItemSkeleton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val characters = viewModel.charactersPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Multiverse Explorer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (characters.loadState.refresh is LoadState.Loading && characters.itemCount == 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(10) {
                        CharacterItemSkeleton()
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        count = characters.itemCount,
                        key = characters.itemKey { it.id }
                    ) { index ->
                        val character = characters[index]
                        if (character != null) {
                            CharacterItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) },
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                        }
                    }

                    if (characters.loadState.append is LoadState.Loading) {
                        items(3) {
                            CharacterItemSkeleton()
                        }
                    }

                    if (characters.loadState.refresh is LoadState.Error) {
                        item {
                            val error = (characters.loadState.refresh as LoadState.Error).error
                            Text(
                                text = "Error: ${error.localizedMessage}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
