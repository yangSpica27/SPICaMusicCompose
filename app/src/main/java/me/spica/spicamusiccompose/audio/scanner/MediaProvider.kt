package me.spica.spicamusiccompose.audio.scanner

import kotlinx.coroutines.flow.Flow
import me.spica.spicamusiccompose.persistence.entity.Song

interface MediaProvider {

    val type: String

    fun findSongs(): Flow<FlowEvent<List<Song>, MessageProgress>>

}