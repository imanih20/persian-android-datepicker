package com.mohyeddin.datepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohyeddin.datepicker.date.DatePicker
import com.mohyeddin.datepicker.date.DatePickerDialog
import com.mohyeddin.datepicker.ui.theme.DatePickerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DatePickerTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold{

                        DP(Modifier.padding(it)){m->

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DP(modifier: Modifier = Modifier,onDateChange: (String)->Unit){
    var date by remember {
        mutableStateOf("")
    }
    var showingDialog by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier){
        OutlinedTextField(
            value = date,
            onValueChange =  {date = it},
            placeholder = {
                Text(text = "1401-10-27")
            },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "" , modifier = Modifier.clickable {
                    showingDialog = true
                })
            }
        )
        if (showingDialog)
            DatePickerDialog(onDismissRequest = { showingDialog = false }, onSubmitClicked = {
                date = it
            })

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DatePickerTheme {
        DatePicker {
        }
    }
}