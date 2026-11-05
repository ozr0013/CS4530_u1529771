package com.example.marblegame.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Manages registration and data processing for Android motion sensors.
 *
 * Uses Gravity Sensor by default, falling back to Accelerometer for emulators.
 * Applies a low-pass filter to smooth accelerometer readings.
 */
class MarbleSensorManager(
    context: Context,
    private val onSensorUpdated: (tiltX: Float, tiltY: Float, timestampNs: Long, sensorName: String) -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private val activeSensor: Sensor? = gravitySensor ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val isUsingAccelerometer = gravitySensor == null && activeSensor != null

    // Low-pass filter variables for accelerometer fallback smoothing
    private val filteredValues = FloatArray(3)
    private val alpha = 0.8f // Filter coefficient

    val activeSensorName: String
        get() = when {
            gravitySensor != null -> "Gravity Sensor"
            activeSensor != null -> "Accelerometer (Emulator Fallback)"
            else -> "No Sensor Available"
        }

    fun startListening() {
        activeSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val rawX: Float
        val rawY: Float

        if (isUsingAccelerometer) {
            // Apply low-pass filter to eliminate jitter on accelerometer
            filteredValues[0] = alpha * filteredValues[0] + (1 - alpha) * event.values[0]
            filteredValues[1] = alpha * filteredValues[1] + (1 - alpha) * event.values[1]
            rawX = filteredValues[0]
            rawY = filteredValues[1]
        } else {
            rawX = event.values[0]
            rawY = event.values[1]
        }

        onSensorUpdated(rawX, rawY, event.timestamp, activeSensorName)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not required for marble physics
    }
}
