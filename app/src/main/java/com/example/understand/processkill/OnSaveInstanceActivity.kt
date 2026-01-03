package com.example.understand.processkill

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.understand.R
import com.example.understand.StateViewModel
import com.example.understand.TAG
import com.example.understand.databinding.ActivityOnSaveInstanceBinding

class OnSaveInstanceActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOnSaveInstanceBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[StateViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.myEditText.doOnTextChanged { _, _, _, _ ->
            viewModel.setCursorPos(binding.myEditText.selectionStart)
        }
        Log.d(TAG, "onCreate: viewModel state value ${viewModel.cursorPosState.value}")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val cursorPos = savedInstanceState.getInt("cursor_pos")
        Log.d(TAG, "onRestoreInstanceState: $cursorPos")
        /*
            * If this would be in onCreate or before super.onRestoreInstanceState method this would give me exception
            * RuntimeException
            * because it has just laid out editText but onRestore has not been called yet and edit text is just blank
        */
        if (cursorPos + 2 < binding.myEditText.text.length) {
            binding.myEditText.setSelection(cursorPos + 2)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cursorPos = binding.myEditText.selectionStart
        Log.d(TAG, "onSaveInstanceState: $cursorPos")
        outState.putInt("cursor_pos", binding.myEditText.selectionStart)
    }
}