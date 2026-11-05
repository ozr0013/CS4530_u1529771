package com.example.marblegame

import com.example.marblegame.viewmodel.MarbleViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MarbleViewModelTest {

    private lateinit var viewModel: MarbleViewModel

    @Before
    fun setUp() {
        viewModel = MarbleViewModel()
    }

    @Test
    fun updateScreenBounds_centersMarble() {
        val width = 1000f
        val height = 2000f
        val marbleSize = 100f

        viewModel.updateScreenBounds(width, height, marbleSize)

        val state = viewModel.marbleState.value
        assertEquals(1000f, state.screenWidthPx)
        assertEquals(2000f, state.screenHeightPx)
        assertEquals(100f, state.marbleSizePx)

        // Center X = (1000 - 100) / 2 = 450
        // Center Y = (2000 - 100) / 2 = 950
        assertEquals(450f, state.xPx, 0.1f)
        assertEquals(950f, state.yPx, 0.1f)
    }

    @Test
    fun resetMarble_resetsPositionToCenterAndZeroesVelocity() {
        val width = 1000f
        val height = 2000f
        val marbleSize = 100f

        viewModel.updateScreenBounds(width, height, marbleSize)
        viewModel.resetMarble()

        val state = viewModel.marbleState.value
        assertEquals(450f, state.xPx, 0.1f)
        assertEquals(950f, state.yPx, 0.1f)
        assertEquals(0f, state.vx, 0.001f)
        assertEquals(0f, state.vy, 0.001f)
    }

    @Test
    fun boundaryClamping_keepsMarbleInsideScreen() {
        val width = 500f
        val height = 500f
        val marbleSize = 50f

        viewModel.updateScreenBounds(width, height, marbleSize)

        val state = viewModel.marbleState.value
        val maxAllowedX = width - marbleSize // 450
        val maxAllowedY = height - marbleSize // 450

        assertTrue(state.xPx >= 0f && state.xPx <= maxAllowedX)
        assertTrue(state.yPx >= 0f && state.yPx <= maxAllowedY)
    }
}
