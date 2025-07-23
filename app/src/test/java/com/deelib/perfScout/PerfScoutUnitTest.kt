package com.deeksha.perfscout

import org.junit.Assert.*
import org.junit.Test

class PerfScoutUnitTest {

    @Test
    fun testMediaQualityLevelEnum() {
        val values = io.github.dee2604.perfscout.model.MediaQualityLevel.values()
        assertTrue(values.contains(io.github.dee2604.perfscout.model.MediaQualityLevel.LOW))
        assertTrue(values.contains(io.github.dee2604.perfscout.model.MediaQualityLevel.MEDIUM))
        assertTrue(values.contains(io.github.dee2604.perfscout.model.MediaQualityLevel.HIGH))
        assertTrue(values.contains(io.github.dee2604.perfscout.model.MediaQualityLevel.ULTRA))
    }
} 