package com.mohyeddin.datepicker.util

import ir.huri.jcal.JalaliCalendar

class Date(private val year: Int, private val month:Int, private val day: Int) {
    constructor() : this(JalaliCalendar().year,JalaliCalendar().month,JalaliCalendar().day)
    val calendar : JalaliCalendar
        get() = JalaliCalendar(year, month, day)
}