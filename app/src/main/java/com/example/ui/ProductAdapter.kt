package com.example.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.Product
import com.example.demoproject.R
import com.example.demoproject.databinding.ItemPhotoBinding


class ProductAdapter(
    var context: Context,
    var productList: List<Product>,
    private var presenter: MainPresenter
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var shadow = 0
        val list = productList[position]
        Log.e("TAG", "onBindViewHolder: -----------${list.shadow}")
        shadow = list.shadow

        val opacity = shadow.toFloat() / 10.0F
        holder.binding.imageProduct.alpha = opacity
        holder.binding.seekBar.progress = shadow
        shadow = shadow

        with(holder) {

            presenter = this@ProductAdapter.presenter
            Glide.with(holder.itemView.context).load(list.image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imageProduct)


            binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progresValue: Int,
                    fromUser: Boolean
                ) {
                    val opacity = progresValue.toFloat() / 10.0F
                    binding.imageProduct.alpha = opacity
                    shadow = progresValue
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    list.shadow = shadow
                    presenter.onItemClick(list)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }



}