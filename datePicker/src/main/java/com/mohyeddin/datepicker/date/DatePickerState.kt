package com.mohyeddin.datepicker.date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.huri.jcal.JalaliCalendar

class DatePickerState(
    initialDate: JalaliCalendar = JalaliCalendar(),
    val colors: DatePickerColors,
    val yearRange: IntRange
) {
    var selected by mutableStateOf(initialDate)
    var yearPickerShowing by mutableStateOf(false)

}