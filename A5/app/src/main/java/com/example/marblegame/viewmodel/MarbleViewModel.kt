package com.example.marblegame.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.marblegame.model.MarbleState
import com.example.marblegame.sensor.MarbleSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for managing Marble Game state and physics simulation.
 *
 * Follows MVVM architecture. Receives sensor readings, applies physics formulas
 * (acceleration, friction, velocity, boundary clamping), and updates StateFlow.
 */
class MarbleViewModel : ViewModel() {

    private val _marbleState = MutableStateFlow(MarbleState())
    val marbleState: StateFlow<MarbleState> = _marbleState.asStateFlow()

    private var sensorManager: MarbleSensorManager? = null
    private var lastTimestampNs: Long = 0L

    companion object {
        private const val ACCELERATION_SCALE = 150f // Scale sensor gravity to pixel acceleration
        private const val FRICTION = 0.94f          // Damping factor for natural marble friction
        private const val MAX_DT = 0.033f           // Max time delta clamp (33ms)
    }

    /**
     * Registers sensor listeners in onResume().
     */
    fun startListening(context: Context) {
        if (sensorManager == null) {
            sensorManager = MarbleSensorManager(context.applicationContext) { tiltX, tiltY, timestampNs, sensorName ->
                processSensorUpdate(tiltX, tiltY, timestampNs, sensorName)
            }
        }
        sensorManager?.startListening()
    }

    /**
     * Unregisters sensor listeners in onPause() to conserve battery and resources.
     */
    fun stopListening() {
        sensorManager?.stopListening()
        lastTimestampNs = 0L
    }

    /**
     * Updates playable screen container boundaries.
     */
    fun updateScreenBounds(widthPx: Float, heightPx: Float, marbleSizePx: Float) {
        _marbleState.update { currentState ->
            val isFirstSetup = currentState.screenWidthPx == 0f || currentState.screenHeightPx == 0f
            val initialX = if (isFirstSetup) (widthPx - marbleSizePx) / 2f else currentState.xPx
            val initialY = if (isFirstSetup) (heightPx - marbleSizePx) / 2f else currentState.yPx

            currentState.copy(
                xPx = initialX.coerceIn(0f, (widthPx - marbleSizePx).coerceAtLeast(0f)),
                yPx = initialY.coerceIn(0f, (heightPx - marbleSizePx).coerceAtLeast(0f)),
                screenWidthPx = widthPx,
                screenHeightPx = heightPx,
                marbleSizePx = marbleSizePx
            )
        }
    }

    /**
     * Processes real-time sensor events and computes marble physics.
     */
    private fun processSensorUpdate(rawTiltX: Float, rawTiltY: Float, timestampNs: Long, sensorName: String) {
        val dt = if (lastTimestampNs == 0L) {
            0.016f // Default ~60fps frame delta on initial reading
        } else {
            ((timestampNs - lastTimestampNs) / 1_000_000_000f).coerceIn(0.001f, MAX_DT)
        }
        lastTimestampNs = timestampNs

        // Sensor X points right (tilt right = move right).
        // Sensor Y points UP the phone, but Compose offset Y points DOWN!
        // Inverting Y aligns the marble motion with screen coordinates.
        val tiltX = -rawTiltX
        val tiltY = rawTiltY

        _marbleState.update { state ->
            if (state.screenWidthPx <= 0f || state.screenHeightPx <= 0f) {
                return@update state.copy(tiltX = tiltX, tiltY = tiltY, sensorName = sensorName)
            }

            val maxAllowedX = (state.screenWidthPx - state.marbleSizePx).coerceAtLeast(0f)
            val maxAllowedY = (state.screenHeightPx - state.marbleSizePx).coerceAtLeast(0f)

            // Physics calculation: v = (v + a * dt) * friction
            var newVx = (state.vx + tiltX * ACCELERATION_SCALE * dt) * FRICTION
            var newVy = (state.vy + tiltY * ACCELERATION_SCALE * dt) * FRICTION

            // Position update: p = p + v * dt
            var newX = state.xPx + newVx * dt
            var newY = state.yPx + newVy * dt

            // Boundary collision handling & clamping
            if (newX <= 0f) {
                newX = 0f
                newVx = 0f
            } else if (newX >= maxAllowedX) {
                newX = maxAllowedX
                newVx = 0f
            }

            if (newY <= 0f) {
                newY = 0f
                newVy = 0f
            } else if (newY >= maxAllowedY) {
                newY = maxAllowedY
                newVy = 0f
            }

            state.copy(
                xPx = newX,
                yPx = newY,
                vx = newVx,
                vy = newVy,
                tiltX = tiltX,
                tiltY = tiltY,
                sensorName = sensorName
            )
        }
    }

    /**
     * Resets marble to screen center and clears velocity.
     */
    fun resetMarble() {
        _marbleState.update { state ->
            val centerX = (state.screenWidthPx - state.marbleSizePx) / 2f
            val centerY = (state.screenHeightPx - state.marbleSizePx) / 2f

            state.copy(
                xPx = centerX.coerceAtLeast(0f),
                yPx = centerY.coerceAtLeast(0f),
                vx = 0f,
                vy = 0f
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}
