package com.example.marblegame.model

/**
 * Data class representing the state of the marble physics simulation and device tilt.
 *
 * @property xPx Marble position X in pixels.
 * @property yPx Marble position Y in pixels.
 * @property vx Marble velocity X in pixels per second.
 * @property vy Marble velocity Y in pixels per second.
 * @property tiltX Device tilt X (accelerometer/gravity X value).
 * @property tiltY Device tilt Y (accelerometer/gravity Y value).
 * @property screenWidthPx Width of the playable screen container in pixels.
 * @property screenHeightPx Height of the playable screen container in pixels.
 * @property marbleSizePx Diameter of the marble in pixels.
 * @property sensorName Active sensor name being used (Gravity vs Accelerometer fallback).
 */
data class MarbleState(
    val xPx: Float = 0f,
    val yPx: Float = 0f,
    val vx: Float = 0f,
    val vy: Float = 0f,
    val tiltX: Float = 0f,
    val tiltY: Float = 0f,
    val screenWidthPx: Float = 0f,
    val screenHeightPx: Float = 0f,
    val marbleSizePx: Float = 0f,
    val sensorName: String = "Detecting..."
)
