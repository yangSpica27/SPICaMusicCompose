package me.spica.spicamusiccompose.persistence.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.spica.spicamusiccompose.persistence.entity.Playlist
import me.spica.spicamusiccompose.persistence.entity.PlaylistSongCrossRef
import me.spica.spicamusiccompose.persistence.entity.PlaylistWithSongs


@Dao
interface PlaylistDao {


    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithSongs(): List<PlaylistWithSongs>

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getAllPlaylist(): Flow<List<Playlist>>


    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId == :playlistId")
    fun getPlaylistsWithSongsWithPlayListId(playlistId: Long): List<PlaylistWithSongs>

    @Transaction
    @Query("DELETE FROM playlist WHERE playlistId ==:playlistId")
    fun deleteList(playlistId: Long)

    @Transaction
    @Insert
    suspend fun insertListItems(songs: List<PlaylistSongCrossRef>)

    @Transaction
    @Insert
    suspend fun insertListItem(songs: PlaylistSongCrossRef)

    @Transaction
    @Insert
    suspend fun insertPlaylist(list: Playlist)

    @Transaction
    @Delete
    suspend fun deleteListItem(song: PlaylistSongCrossRef)


    @Transaction
    @Delete
    suspend fun deleteListItems(songs: List<PlaylistSongCrossRef>)

}