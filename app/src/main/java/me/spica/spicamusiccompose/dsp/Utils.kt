package me.spica.spicamusiccompose.dsp

import kotlin.math.log10
import kotlin.math.pow

fun Double.fromDb(): Double {
    return 10.0.pow(this / 20.0)
}

fun Double.toDb(): Double {
    return 20 * log10(this)
}
