package com.example.understand.task

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.understand.ui.theme.MyTheme

class A: AppCompatActivity() {

    companion object {
        private const val TAG = com.example.understand.TAG +" Activity_A"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        setContent {
            MyTheme {
                var textValue by rememberSaveable { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeContentPadding()
                        .padding(16.dp),
                ) {
                    Text("Activity A")

                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Enter text in A") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )

                    Button(onClick = {
                        val intent = Intent(this@A, B::class.java)
                        startActivity(intent)
                    }) {
                        Text("Go to Activity B")
                    }

                    Button(onClick = {
                        val intent = Intent(this@A, B::class.java).apply {
                        }
                        startActivity(intent)
                    }) {
                        Text("Go to Activity B in separate task")
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: ")
    }
}