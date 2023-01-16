package com.mohyeddin.datepicker.date

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.mohyeddin.datepicker.R
import com.mohyeddin.datepicker.util.Date
import com.mohyeddin.datepicker.util.MyText
import com.mohyeddin.datepicker.util.getDoubleKey
import com.mohyeddin.datepicker.util.isSmallDevice
import ir.huri.jcal.JalaliCalendar
import kotlinx.coroutines.launch

@Composable
fun DatePicker(
    initialDate : Date = Date(),
    title: String = "انتخاب تاریخ",
    colors: DatePickerColors = DatePickerDefaults.colors(),
    yearRange: IntRange = IntRange(1390, 1410),
    onDateChange: (String) -> Unit = {}
){
    val datePickerState by remember {
        mutableStateOf(
            DatePickerState(initialDate.calendar, colors, yearRange)
        )
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        DatePickerImpl(
            title = title,
            state = datePickerState
        ){year, month, day ->
            datePickerState.selected.year = year
            datePickerState.selected.month = month
            datePickerState.selected.day = day
            onDateChange(datePickerState.selected.toString())
            datePickerState.selected
        }
        DisposableEffect(datePickerState.selected) {
            onDateChange(datePickerState.selected.toString())
            onDispose { }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DatePickerImpl(
    title: String,
    state: DatePickerState,
    onDateChange: (year: Int,month: Int,day: Int)-> JalaliCalendar
){
    val pagerState = rememberPagerState(
        initialPage = (state.selected.year - state.yearRange.first) * 12 + state.selected.month - 1
    )
    var selectedHeader by remember(state.selected) {
        mutableStateOf(state.selected.dayOfWeekDayMonthString+" "+state.selected.year)
    }
    Column(Modifier.fillMaxWidth()) {
        CalendarHeader(title,state.colors,selectedHeader)
        HorizontalPager(
            count = (state.yearRange.last - state.yearRange.first + 1) * 12,
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.height(336.dp)
        ) { page ->
            val viewYear by remember(page){
                mutableStateOf(state.yearRange.first + page / 12)
            }
            val viewMonth by remember(page) {
                mutableStateOf(page % 12 + 1)
            }
            val viewDate by remember {
                mutableStateOf(JalaliCalendar(
                    viewYear,viewMonth,1
                ))
            }


            Column {
                CalendarViewHeader(viewDate.monthString+" "+viewYear, state.colors,state.yearPickerShowing, pagerState){
                    state.yearPickerShowing = !state.yearPickerShowing
                }
                Box {
                    androidx.compose.animation.AnimatedVisibility(
                        state.yearPickerShowing,
                        modifier = Modifier
                            .zIndex(0.7f)
                            .clipToBounds(),
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it })
                    ) {
                        YearPicker(viewYear, state.yearRange,state.colors, pagerState){
                            state.yearPickerShowing = false
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        !state.yearPickerShowing,
                        modifier = Modifier
                            .zIndex(0.7f)
                            .clipToBounds(),
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it })
                    ) {
                        CalendarView(viewDate, state.selected,state.colors){
                            val date = onDateChange(viewYear,viewMonth,it)
                            selectedHeader = date.dayOfWeekDayMonthString+" "+date.year
                            date.getDoubleKey()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarView(
    viewDate: JalaliCalendar,
    selectedDate: JalaliCalendar,
    colors: DatePickerColors,
    today: JalaliCalendar = JalaliCalendar(),
    onDaySelected: (Int)->Double
) {
    Column(
        Modifier
            .padding(start = 12.dp, end = 12.dp)
            .testTag("dialog_date_calendar")
    ) {
        DayOfWeekHeader(colors.calendarHeaderTextColor)
        val datesList = remember { IntRange(1, viewDate.monthLength).toList() }
        var selected by remember {
            mutableStateOf(selectedDate.getDoubleKey())
        }
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.height(240.dp)) {
            for (x in 0 until viewDate.also { it.day = 1 }.dayOfWeek %7) {
                item { Box(Modifier.size(40.dp)) }
            }

            items(datesList) { item ->
                val itemKey = viewDate.year + viewDate.month * 0.01 + item * 0.0001
                DateSelectionBox(item, selected == itemKey,today.getDoubleKey() == itemKey, colors) {
                    selected = onDaySelected(item)
                }
            }
        }
    }
}

@Composable
fun DateSelectionBox(
    date: Int,
    selected: Boolean,
    isToday : Boolean,
    colors: DatePickerColors,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .testTag("dialog_date_selection_$date")
            .size(40.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = { onClick() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        MyText(
            date.toString(),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colors.dateBackgroundColor(selected).value)
                .border(
                    BorderStroke(
                        1.dp,
                        if (isToday) MaterialTheme.colorScheme.primary else Color.Transparent
                    ), shape = CircleShape
                )
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                    color = colors.dateTextColor(selected).value,
                    fontSize = 12.sp
                )
            )
    }
}

@Composable
private fun DayOfWeekHeader(headerTextColor: Color) {
//    val dayHeaders = dateHeader.firstDayOfWeek.let { firstDayOfWeek ->
//        (0L until 7L).map {l->
//            dateHeader.apply { day = firstDayOfWeek.plus(l).toInt() }
//            dateHeader.dayOfWeekString
//        }
//    }
    val dayHeaders = listOf(
        "ش",
        "ی",
        "د",
        "س",
        "چ",
        "پ",
        "ج"
    )

    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            dayHeaders.forEach {
                item {
                    Box(Modifier.size(40.dp)) {
                        MyText(
                            it,
                            modifier = Modifier
                                .alpha(0.8f)
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            style = TextStyle(fontSize = 14.sp, fontWeight = W600),
                            color = headerTextColor
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun YearPicker(year: Int, yearRange: IntRange,colors: DatePickerColors, pagerState: PagerState,onYearSelected:()->Unit) {
    val gridState = rememberLazyGridState(year - yearRange.first)
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
//        modifier = Modifier.background(state.dialogBackground)
    ) {
        itemsIndexed(yearRange.toList()) { _, item ->
            val selected = remember { item == year }
            YearPickerItem(year = item, selected = selected, colors = colors) {
                if (!selected) {
                    scope.launch{
                        pagerState.scrollToPage(
                            pagerState.currentPage + (item - year) * 12
                        )
                    }
                }
                onYearSelected()
            }
        }
    }
}

@Composable
private fun YearPickerItem(year: Int, selected: Boolean, colors: DatePickerColors, onClick:() -> Unit) {

    Box(Modifier.size(88.dp, 52.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(72.dp, 36.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.dateBackgroundColor(selected).value)
                .clickable(
                    onClick = onClick,
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ),
            contentAlignment = Alignment.Center
        ) {
            MyText(
                year.toString(),
                style = TextStyle(
                    color = colors.dateTextColor(selected).value,
                    fontSize = 18.sp
                )
            )

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun CalendarViewHeader(yearTitle: String, colors: DatePickerColors,yearPickerShowing:Boolean, pagerState: PagerState,onYearClick:()->Unit) {
    val coroutineScope = rememberCoroutineScope()
    val arrowDropUp = painterResource(id = R.drawable.ic_baseline_arrow_drop_up_24)
    val arrowDropDown = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24)

    Box(
        Modifier
            .padding(top = 16.dp, bottom = 16.dp, start = 24.dp, end = 24.dp)
            .height(24.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .clickable(onClick = onYearClick)
        ) {
            MyText(
                yearTitle,
                modifier = Modifier
                    .paddingFromBaseline(top = 16.dp)
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(fontSize = 14.sp, fontWeight = W600),
                color = colors.calendarHeaderTextColor
            )

            Spacer(Modifier.width(4.dp))
            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Icon(
                    if (yearPickerShowing) arrowDropUp else arrowDropDown,
                    contentDescription = "Year Selector",
                    tint = colors.calendarHeaderTextColor
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Previous Month",
                modifier = Modifier
                    .testTag("dialog_date_previous_month")
                    .size(24.dp)
                    .clickable(onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage - 1 >= 0) {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage - 1
                                )
                            }
                        }
                    }),
                tint = colors.calendarHeaderTextColor
            )

            Spacer(modifier = Modifier.width(24.dp))

            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Next Month",
                modifier = Modifier
                    .testTag("dialog_date_next_month")
                    .size(24.dp)
                    .clickable(onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage + 1 < pagerState.pageCount) {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1
                                )
                            }
                        }
                    }),
                tint = colors.calendarHeaderTextColor
            )

        }
    }
}

@Composable
private fun CalendarHeader(title: String,colors: DatePickerColors,selectedHeader : String) {


    Box(
        Modifier
            .background(colors.headerBackgroundColor)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp)) {
            MyText(
                text = title,
                modifier = Modifier.paddingFromBaseline(top = if (isSmallDevice()) 24.dp else 32.dp),
                color = colors.headerTextColor,
                style = TextStyle(fontSize = 12.sp)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(top = if (isSmallDevice()) 0.dp else 64.dp)
            ) {
                MyText(
                    text = selectedHeader,
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = colors.headerTextColor,
                    style = TextStyle(fontSize = 30.sp, fontWeight = W400)
                )
            }

            Spacer(Modifier.height(if (isSmallDevice()) 8.dp else 16.dp))
        }
    }
}

