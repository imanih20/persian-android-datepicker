package com.mohyeddin.datepicker.util

import ir.huri.jcal.JalaliCalendar

internal fun JalaliCalendar.getDoubleKey() : Double = year + month * 0.01 + day * 0.0001

internal fun String.withPersianNumber() : String {
    var newString = toString()
    for (c in toCharArray()) {
        if (c.isDigit()){
            newString = when(c){
                '0'-> newString.replace(c,'۰')
                '1'-> newString.replace(c,'۱')
                '2'-> newString.replace(c,'۲')
                '3'-> newString.replace(c,'۳')
                '4'-> newString.replace(c,'۴')
                '5'-> newString.replace(c,'۵')
                '6'-> newString.replace(c,'۶')
                '7'-> newString.replace(c,'۷')
                '8'-> newString.replace(c,'۸')
                '9'-> newString.replace(c,'۹')
                else -> newString
            }
        }
    }
    return newString
}


//internal val LocalDate.yearMonth: YearMonth
//    get() = YearMonth.of(this.year, this.month)


//internal fun Month.getShortLocalName(locale: Locale): String =
//    this.getDisplayName(java.time.format.TextStyle.SHORT, locale)
//
//internal fun Month.getFullLocalName(locale: Locale) =
//    this.getDisplayName(java.time.format.TextStyle.FULL_STANDALONE, locale)
//
//internal fun DayOfWeek.getShortLocalName(locale: Locale) =
//    this.getDisplayName(java.time.format.TextStyle.SHORT, locale)
