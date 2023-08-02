package com.example.bloodpressure.domain

import com.example.bloodpressure.data.Record

object RecordValidator {
    fun validateRecord(record: Record): ValidationResult{
        val result = ValidationResult()

        if(record.diastolicPressure.isBlank()){result.copy(
            diastolicPressureError = "Diastolic pressure cannot be empty"
        )}

        if (record.systolicPressure.isBlank()){result.copy(
            systolicPressureError = "Systolic pressure cannot be empty"
        )}

        if(record.pulse.isBlank()){result.copy(
            pulseError = "Pulse cannot be empty"
        )}

        return result
    }
}

data class ValidationResult(
    val systolicPressureError: String? = null,
    val diastolicPressureError: String? = null,
    val pulseError: String? = null
)