package com.example.bloodpressure.presentation.components.graph

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import com.example.bloodpressure.data.DataPoint
import com.example.bloodpressure.data.Record
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Graph(
    records: List<Record>,
    modifier: Modifier = Modifier
) {
    val dataPoints = records.mapIndexed { index, record ->
        DataPoint(
            record,
            (index+1).toFloat()
        )
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps(dataPoints.size-1)
        .labelData {
            it.toString()
        }
        .labelAndAxisLinePadding(25.dp)
        .axisLineColor(MaterialTheme.colorScheme.onSurface)
        .axisLabelColor(MaterialTheme.colorScheme.onSurface)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(4)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = 50
            (i * yScale).toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.onSurface)
        .axisLabelColor(MaterialTheme.colorScheme.onSurface)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = dataPoints.map {
                        it.diaPoint
                    },
                    LineStyle(
                        lineType = LineType.SmoothCurve(
                            isDotted = false,
                        ),
                        color = Color.Red
                    )
                ),
                Line(
                    dataPoints = dataPoints.map {
                        it.sysPoint
                    },
                    LineStyle(
                        lineType = LineType.SmoothCurve(
                            isDotted = false
                        ),
                        color = Color.Green
                    )
                ),
                Line(
                    dataPoints = dataPoints.map {
                        it.pulPoint
                    },
                    LineStyle(
                        lineType = LineType.SmoothCurve(),
                        color = Color.Blue
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        isZoomAllowed = true,
        backgroundColor = MaterialTheme.colorScheme.surface
    )

    LineChart(
        modifier = modifier,
        lineChartData = lineChartData
    )
}

fun Long.toDateNoYears(): String{
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM", Locale.ENGLISH)
    return format.format(date)
}