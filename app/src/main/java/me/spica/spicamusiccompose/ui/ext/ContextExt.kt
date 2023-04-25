package me.spica.spicamusiccompose.ui.ext

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import me.spica.spicamusiccompose.ui.main.MainActivity

const val DEFAULT_RES_CODE = 0xA0C0
fun Context.newBroadcastPendingIntent(action: String): PendingIntent =
    PendingIntent.getBroadcast(
        this,
        DEFAULT_RES_CODE,
        Intent(action).setFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY),
        PendingIntent.FLAG_IMMUTABLE
    )


fun Context.newMainPendingIntent(): PendingIntent =
    PendingIntent.getActivity(
        this,
        DEFAULT_RES_CODE,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )