package com.example.rickandmorty.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.rickandmorty.core.database.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCharacterDatabase(
        @ApplicationContext context: Context
    ): CharacterDatabase {
        return Room.databaseBuilder(
            context,
            CharacterDatabase::class.java,
            "character_db"
        ).build()
    }
}
