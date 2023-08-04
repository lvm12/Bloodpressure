package com.example.bloodpressure.data

import com.example.bloodpressure.data.csv.CSVGenerationStatus

sealed interface BloodPressureEvent {
    object OnAddNewRecordClicked: BloodPressureEvent
    object SaveRecord: BloodPressureEvent
    object OnExportAsCSVClicked: BloodPressureEvent
    data class EditRecord(val record: Record): BloodPressureEvent
    object DismissRecord: BloodPressureEvent
    data class SelectRecord(val record: Record): BloodPressureEvent
    object DeleteRecord: BloodPressureEvent
    data class OnSystolicPressureChanged(val value: String): BloodPressureEvent
    data class OnDiastolicPressureChanged(val value: String): BloodPressureEvent
    data class OnPulseChanged(val value: String): BloodPressureEvent
    object OnActionsClicked: BloodPressureEvent
    object OnDoneClickedCSV: BloodPressureEvent
    data class ONCSVGenerated(val status: CSVGenerationStatus): BloodPressureEvent
    data class PermissionProvided(val value: Boolean): BloodPressureEvent
    data class NewSelected(val value: Boolean): BloodPressureEvent
    data class ExportedSelected(val value: Boolean): BloodPressureEvent
    data class ArchivedSelected(val value: Boolean): BloodPressureEvent
    data class ArchiveRecord(val record: Record?): BloodPressureEvent
    object DeleteArchived: BloodPressureEvent
    object SetNew: BloodPressureEvent
    object WHYYYYYYY: BloodPressureEvent
    data class RecordUpdateStatus(val status: com.example.bloodpressure.data.RecordUpdateStatus): BloodPressureEvent
}