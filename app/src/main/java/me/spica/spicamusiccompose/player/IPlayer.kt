package me.spica.spicamusiccompose.player

import me.spica.spicamusiccompose.persistence.entity.Song

interface IPlayer {


    fun loadSong(song: Song?, play: Boolean)

    fun getState(durationMs: Long): State

    fun seekTo(positionMs: Long)

    fun setPlaying(isPlaying: Boolean)


    class State(
        val isPlaying: Boolean,
        val currentPositionMs: Long,
    ) {


        companion object {

            fun from(isPlaying: Boolean, isAdvancing: Boolean, positionMs: Long) =
                State(
                    isPlaying,
                    positionMs
                )
        }

    }
}