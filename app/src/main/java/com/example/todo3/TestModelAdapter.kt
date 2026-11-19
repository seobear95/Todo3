package com.example.todo3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo3.databinding.LayoutTestmodelViewholderBinding

class TestModelAdapter : ListAdapter<TestModel, TestModelHolder>(MyDiffUtil) {

    private var onActionClick: OnActionClick? = null

    object MyDiffUtil : DiffUtil.ItemCallback<TestModel>() {
        override fun areItemsTheSame(oldItem: TestModel, newItem: TestModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TestModel, newItem: TestModel): Boolean {
            return oldItem.date == newItem.date && oldItem.description == newItem.description
        }
    }

    override fun onBindViewHolder(holder: TestModelHolder, position: Int) {
        holder.bindView(getItem(position), this.onActionClick)
    }

    fun setOnActionClickListener(onActionClick: OnActionClick) {
        this.onActionClick = onActionClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestModelHolder {
        return TestModelHolder.create(parent)
    }
}

class TestModelHolder(private val binding: LayoutTestmodelViewholderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(testModel: TestModel, onActionClick: OnActionClick?) {
        binding.tvTitle.text = testModel.date
        binding.tvDescription.text = testModel.description
        binding.ivDelete.setOnClickListener {
            onActionClick?.onClickDelete(it, testModel)
        }
        binding.ivUpdate.setOnClickListener {
            onActionClick?.onClickUpdate(it, testModel)
        }
    }

    companion object {
        fun create(parent: ViewGroup): TestModelHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = LayoutTestmodelViewholderBinding.inflate(layoutInflater, parent, false)
            return TestModelHolder(binding)
        }
    }
}

interface OnActionClick {
    fun onClickDelete(view: View, testModel: TestModel)
    fun onClickUpdate(it: View?, testModel: TestModel)
}
