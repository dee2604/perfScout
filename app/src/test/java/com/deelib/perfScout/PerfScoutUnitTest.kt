package com.deelib.perfScout

import org.junit.Assert.*
import org.junit.Test

class PerfScoutUnitTest {

    @Test
    fun testMediaQualityLevelEnum() {
        val values = com.deelib.perfScout.model.MediaQualityLevel.values()
        assertTrue(values.contains(com.deelib.perfScout.model.MediaQualityLevel.LOW))
        assertTrue(values.contains(com.deelib.perfScout.model.MediaQualityLevel.MEDIUM))
        assertTrue(values.contains(com.deelib.perfScout.model.MediaQualityLevel.HIGH))
        assertTrue(values.contains(com.deelib.perfScout.model.MediaQualityLevel.ULTRA))
    }
} 