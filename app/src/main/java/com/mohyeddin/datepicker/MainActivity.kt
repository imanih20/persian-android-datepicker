package com.mohyeddin.datepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    var date by remember {
                        mutableStateOf("")
                    }
                    var showDialog by remember {
                        mutableStateOf(false)
                    }
                    Scaffold{
                        Column(
                            Modifier
                                .padding(it)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = date)
                            Spacer(modifier = Modifier.size(15.dp))
                            Button(onClick = {
                                showDialog = true
                            }) {
                                Text(text = "date picker")
                            }
                            if (showDialog)
                                DatePickerDialog(
                                    onDismissRequest = { showDialog = false },
                                    onSubmitClicked = {d->
                                        date = d
                                    }
                                )
                        }
                    }
                }
            }
        }
    }
}

