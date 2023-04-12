package me.spica.spicamusiccompose.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import me.spica.spicamusiccompose.persistence.dao.PlaylistDao
import me.spica.spicamusiccompose.persistence.dao.SongDao
import me.spica.spicamusiccompose.persistence.entity.Playlist
import me.spica.spicamusiccompose.persistence.entity.PlaylistSongCrossRef
import me.spica.spicamusiccompose.persistence.entity.Song

/**
 * 数据库
 */
@Database(entities = [Song::class, Playlist::class, PlaylistSongCrossRef::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    abstract fun playlistDao(): PlaylistDao

}