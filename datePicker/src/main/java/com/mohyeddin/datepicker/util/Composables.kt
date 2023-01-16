package com.mohyeddin.datepicker.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration


//@Composable
//internal fun DialogTitle(text: String, modifier: Modifier = Modifier) {
//    Text(
//        text,
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentWidth(Alignment.CenterHorizontally),
//        color = MaterialTheme.colorScheme.onBackground,
//        fontSize = 20.sp,
//        style = TextStyle(fontWeight = FontWeight.W600)
//    )
//}

@Composable
internal fun isSmallDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp <= 360
}
//
//@Composable
//internal fun isLargeDevice(): Boolean {
//    return LocalConfiguration.current.screenWidthDp <= 600
//}