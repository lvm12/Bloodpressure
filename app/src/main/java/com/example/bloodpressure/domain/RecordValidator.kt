package com.example.bloodpressure.domain

import com.example.bloodpressure.data.Record

object RecordValidator {
    fun validateRecord(record: Record): ValidationResult{
        var result = ValidationResult()

        if(record.diastolicPressure.isBlank()){result = result.copy(
            diastolicPressureError = "Diastolic pressure cannot be empty"
        )}

        if (record.systolicPressure.isBlank()){result =result.copy(
            systolicPressureError = "Systolic pressure cannot be empty"
        )}

        if(record.pulse.isBlank()){result = result.copy(
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