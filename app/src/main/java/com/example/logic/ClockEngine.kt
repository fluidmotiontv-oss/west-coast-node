package com.example.logic

import java.time.LocalTime
import java.time.temporal.ChronoField

/**
 * Dragon 9 Sovereign Clock Engine
 * Operates on Fluid Motion Time (reallocating the 1,440-minute daily solar cycle).
 */
object ClockEngine {

    data class FluidTime(
        val hour: Int,
        val minute: Int,
        val isApex: Boolean,
        val apexName: String? = null
    )

    fun getCurrentFluidTime(): FluidTime {
        val now = LocalTime.now()
        val minutesSinceMidnight = now.get(ChronoField.MINUTE_OF_DAY)

        // 18-Minute Apex Wedges
        // Midday Apex: Minute 711.0 to 729.0 (Centered at 12:00 PM)
        // Midnight Apex: Minute 1431.0 to 9.0 (Centered at 00:00 AM)
        
        if (minutesSinceMidnight >= 711 && minutesSinceMidnight < 729) {
            return FluidTime(0, minutesSinceMidnight - 711, true, "Midday Apex")
        }
        
        if (minutesSinceMidnight >= 1431 || minutesSinceMidnight < 9) {
            val m = if (minutesSinceMidnight >= 1431) minutesSinceMidnight - 1431 else minutesSinceMidnight + 9
            return FluidTime(0, m, true, "Midnight Apex")
        }

        // Standard 26 Operational Hours × 54 Minutes = 1,404 Operational Minutes
        // Need to calculate which "operational minute" we are in, excluding the apex gaps.
        
        var operationalMinute = minutesSinceMidnight
        if (minutesSinceMidnight >= 729) {
            operationalMinute -= 18
        }
        if (minutesSinceMidnight >= 9) {
            operationalMinute -= 9
        } else {
            // This part is tricky because midnight apex wraps around.
            // If it's between 0 and 9, it's apex.
            // If it's before 1431, we haven't hit the midnight apex yet.
        }
        
        // Re-calibrating for the 1404 minutes
        val totalOperationalMinutes = if (minutesSinceMidnight >= 1431) {
             0 // Should have been caught by apex check
        } else if (minutesSinceMidnight >= 729) {
            minutesSinceMidnight - 18 - 9
        } else if (minutesSinceMidnight >= 9) {
            minutesSinceMidnight - 9
        } else {
            0 // Should have been caught by apex check
        }

        val hour = totalOperationalMinutes / 54
        val minute = totalOperationalMinutes % 54

        return FluidTime(hour + 1, minute, false)
    }
}
