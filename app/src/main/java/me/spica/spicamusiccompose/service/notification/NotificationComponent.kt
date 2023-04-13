package me.spica.spicamusiccompose.service.notification

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import me.spica.spicamusiccompose.service.ForegroundServiceNotification
import me.spica.spicamusiccompose.service.MusicService
import me.spica.spicamusiccompose.BuildConfig
import me.spica.spicamusiccompose.R
import me.spica.spicamusiccompose.ext.newBroadcastPendingIntent
import me.spica.spicamusiccompose.ext.newMainPendingIntent
import me.spica.spicamusiccompose.playback.RepeatMode

@SuppressLint("RestrictedApi")
class NotificationComponent(private val context: Context, sessionToken: MediaSessionCompat.Token) :
    ForegroundServiceNotification(context, CHANNEL_INFO) {


    override val code: Int
        get() = 0x102030

    init {
        setSmallIcon(R.mipmap.ic_launcher)
        setCategory(NotificationCompat.CATEGORY_TRANSPORT)
        setShowWhen(false)
        setSilent(true)
        setContentIntent(context.newMainPendingIntent())
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        addAction(buildRepeatAction(context, RepeatMode.NONE))
        addAction(buildAction(context, MusicService.ACTION_SKIP_PREV, R.drawable.ic_pre))
        addAction(buildPlayPauseAction(context, true))
        addAction(buildAction(context, MusicService.ACTION_SKIP_NEXT, R.drawable.ic_next))
        addAction(buildAction(context, MusicService.ACTION_EXIT, R.drawable.ic_close))

        setStyle(
            androidx.media.app
                .NotificationCompat
                .MediaStyle()
                .setMediaSession(sessionToken)
                .setShowActionsInCompactView(1, 2, 3)
        )
    }


    fun updateMetadata(metadata: MediaMetadataCompat) {
        setLargeIcon(metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
        setContentTitle(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
        setContentText(metadata.getText(MediaMetadataCompat.METADATA_KEY_ARTIST))
        setSubText(metadata.getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION))
    }

    private fun buildPlayPauseAction(
        context: Context,
        isPlaying: Boolean
    ): NotificationCompat.Action {
        val drawableRes =
            if (isPlaying) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }
        return buildAction(context, MusicService.ACTION_PLAY_PAUSE, drawableRes)
    }

    private fun buildRepeatAction(
        context: Context,
        repeatMode: RepeatMode
    ): NotificationCompat.Action {
        return buildAction(context, MusicService.ACTION_INC_REPEAT_MODE, repeatMode.icon)
    }

    private fun buildAction(context: Context, actionName: String, @DrawableRes iconRes: Int) =
        NotificationCompat.Action.Builder(
            iconRes, actionName, context.newBroadcastPendingIntent(actionName)
        )
            .build()


    fun updatePlaying(isPlaying: Boolean) {
        mActions[2] = buildPlayPauseAction(context, isPlaying)
    }


    fun updateRepeatMode(repeatMode: RepeatMode) {
        mActions[0] = buildRepeatAction(context, repeatMode)
    }


    private companion object {
        val CHANNEL_INFO =
            ChannelInfo(
                id = BuildConfig.APPLICATION_ID + ".channel.PLAYBACK",
                nameRes = R.string.playing
            )
    }
}