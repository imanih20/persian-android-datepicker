package com.mohyeddin.datepicker.date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import ir.huri.jcal.JalaliCalendar

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onSubmitClicked: (String) -> Unit,
    title: String = "انتخاب تاریخ",
    initialDay: Int = JalaliCalendar().day,
    initialMonth: Int = JalaliCalendar().month,
    initialYear: Int = JalaliCalendar().year,
    colors: DatePickerColors = DatePickerDefaults.colors(),
    yearRange: IntRange = IntRange(1390,1410)
){
    var date by remember {
        mutableStateOf(JalaliCalendar().toString())
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest = onDismissRequest, DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = true,
            securePolicy = SecureFlagPolicy.SecureOn
        )) {
            Card {
                Column {
                    DatePicker(
                        initialDay,
                        initialMonth,
                        initialYear,
                        title,
                        colors,
                        yearRange
                    ){
                        date = it
                    }
                    Row {
                        TextButton(onClick = {
                            onSubmitClicked(date)
                            onDismissRequest()
                        }) {
                            Text(text = "ثبت")
                        }
                        TextButton(onClick = { onDismissRequest() }) {
                            Text(text = "لغو")
                        }
                    }
                }
            }
        }
    }
}