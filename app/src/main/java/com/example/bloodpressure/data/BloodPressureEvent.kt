package com.example.bloodpressure.data


import android.net.Uri
import com.example.bloodpressure.data.csv.CSVGenerationStatus
import com.example.bloodpressure.data.sql.records.SavedUri

sealed interface BloodPressureEvent {
    object requestPermission: BloodPressureEvent
    object getUri: BloodPressureEvent
    data class setUri(val uri: Uri): BloodPressureEvent
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
    data class OnCommentChanged(val value: String): BloodPressureEvent
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
    data class RecordUpdaterStatus(val status: RecordUpdateStatus): BloodPressureEvent
    object OnGraphScreenClicked: BloodPressureEvent
}