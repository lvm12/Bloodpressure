package com.example.bloodpressure.data

import co.yml.charts.common.model.Point

data class DataPoint(
    val record: Record,
    val number: Float
){
    var sysPoint: Point = Point(x = number, y = record.systolicPressure.toFloat())
    var diaPoint: Point = Point(x = number, y = record.diastolicPressure.toFloat())
    var pulPoint: Point = Point(x = number, y = record.pulse.toFloat())
}
