package com.example.bloodpressure.domain

import androidx.core.text.isDigitsOnly
import com.example.bloodpressure.data.Record

object RecordValidator {
    fun validateRecord(record: Record): ValidationResult{
        var result = ValidationResult()

        if(record.diastolicPressure.isBlank() && record.diastolicPressure.isDigitsOnly()){result = result.copy(
            diastolicPressureError = "Diastolic pressure cannot be empty"
        )}

        if (record.systolicPressure.isBlank() && record.systolicPressure.isDigitsOnly()){result =result.copy(
            systolicPressureError = "Systolic pressure cannot be empty"
        )}

        if(record.pulse.isBlank() && record.systolicPressure.isDigitsOnly()){result = result.copy(
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