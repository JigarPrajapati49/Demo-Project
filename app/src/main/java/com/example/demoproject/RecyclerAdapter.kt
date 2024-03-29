package com.example.demoproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.databinding.ItemSingleRowBinding


class TestAdapter(private val productList: ArrayList<String>) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(private val binding: ItemSingleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                name = item
            }
        }
    }


}