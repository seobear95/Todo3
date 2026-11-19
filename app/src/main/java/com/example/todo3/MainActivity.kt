package com.example.todo3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo3.databinding.ActivityMainBinding
import com.example.todo3.databinding.DialogInputBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var testModelAdapter: TestModelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
        testModelAdapter = TestModelAdapter()
        binding.RecyclerView.adapter = testModelAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.allTestModels.observe(this) {
            Toast.makeText(this, "${it.size}", Toast.LENGTH_SHORT).show()
            testModelAdapter.submitList(it)
        }

        binding.btnInsert.setOnClickListener {
            val nextIntent = Intent(this, SecondActivity::class.java).apply {
                putExtra("id", -1L)
            }
            startActivity(nextIntent)
        }

        testModelAdapter.setOnActionClickListener(object : OnActionClick {
            override fun onClickDelete(view: View, testModel: TestModel) {
                deleteTestModel(testModel)
                viewModel.selectAllTestModel()
            }

            override fun onClickUpdate(it: View?, testModel: TestModel) {
                updateTestModel(testModel)
                viewModel.selectAllTestModel()
            }
        })

        viewModel.selectAllTestModel()
    }

    private fun updateTestModel(testModel: TestModel) {
        val dlayoutInflater = LayoutInflater.from(this)
        val dbinding = DialogInputBinding.inflate(dlayoutInflater)
        dbinding.edDialogTitle.setText(testModel.date)
        dbinding.edDialogDes.setText(testModel.description)
        val builder = AlertDialog.Builder(this)
        builder.setView(dbinding.root)
        builder.setTitle("update Test")
        builder.setMessage(null)
        val updateDialog = builder.create()
        updateDialog.show()

        dbinding.btnDialogUpdate.setOnClickListener {
            val title = dbinding.edDialogTitle.text.toString()
            val des = dbinding.edDialogDes.text.toString()
            this.viewModel.updateTestModel(testModel.id.orEmpty(), title, des)
            this.viewModel.selectAllTestModel()
            Toast.makeText(this, "항목이 수정 되었습니다!", Toast.LENGTH_SHORT).show()
            updateDialog.dismiss()
        }
    }

    private fun deleteTestModel(testModel: TestModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("DB에 저장된 항목 삭제")
        builder.setMessage("선택된 항목을 정말 삭제 할지요?")
        builder.setIcon(R.drawable.baseline_delete_outline_24)
        builder.setPositiveButton("네") { d, _ ->
            this.viewModel.deleteTestModel(testModel.id.orEmpty())
            this.viewModel.selectAllTestModel()
            Toast.makeText(this, "항목이 삭제되었습니다!", Toast.LENGTH_SHORT).show()
            d.dismiss()
        }
        builder.setNegativeButton("아니오") { d, _ ->
            d.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.selectAllTestModel()
    }
}
