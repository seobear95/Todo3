package com.example.todo3

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.todo3.databinding.ActivitySecondBinding
import java.util.Calendar

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var viewModel: MainViewModel
    val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val currentTime: Long = System.currentTimeMillis()
        val initialDate = DateFormat.format("yyyy/MM/dd", currentTime).toString()
        binding.edTitle.setText(initialDate)
        val initialModel = viewModel.selectToDo(initialDate)
        binding.edDescription.setText(initialModel?.description)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val selectedDate = DateFormat.format("yyyy/MM/dd", calendar.timeInMillis).toString()
            binding.edTitle.setText(selectedDate)
            val testModel = viewModel.selectToDo(selectedDate)
            binding.edDescription.setText(testModel?.description)
        }

        binding.btnInsert.setOnClickListener {
            insertTestModel()
        }

        binding.btnSelectAll.setOnClickListener {
            finish()
        }
    }

    private fun insertTestModel() {
        val title = binding.edTitle.text.toString()
        val des = binding.edDescription.text.toString()
        this.viewModel.addTestModel(title, des)
        Toast.makeText(this, "객체가 저장됨!", Toast.LENGTH_SHORT).show()
        this.viewModel.selectAllTestModel()
    }
}
