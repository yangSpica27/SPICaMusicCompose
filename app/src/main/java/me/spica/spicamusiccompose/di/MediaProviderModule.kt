package me.spica.spicamusiccompose.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.spica.spicamusiccompose.audio.scanner.MediaStoreMediaProvider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MediaProviderModule {

    @Provides
    @Singleton
    fun provideMediaStoreSongProvider(@ApplicationContext context: Context): MediaStoreMediaProvider {
        return MediaStoreMediaProvider(context)
    }
}