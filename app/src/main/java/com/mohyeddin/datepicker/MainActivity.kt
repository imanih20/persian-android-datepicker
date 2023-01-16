package com.mohyeddin.datepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mohyeddin.datepicker.date.DatePicker
import com.mohyeddin.datepicker.ui.theme.DatePickerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = SnackbarHostState()
            val scope = rememberCoroutineScope()

            DatePickerTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState){
                                Snackbar {
                                    Text(text = it.visuals.message)
                                }
                            }
                        }
                    ) {

                        DP(Modifier.padding(it)){m->
                            scope.launch {
                                snackbarHostState.showSnackbar(m)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DP(modifier: Modifier = Modifier,onDateChange: (String)->Unit){
    Box(modifier = modifier){
        DatePicker(onDateChange = onDateChange)
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