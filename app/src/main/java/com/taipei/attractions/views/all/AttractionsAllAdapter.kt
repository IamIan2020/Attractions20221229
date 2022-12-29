package com.taipei.attractions.views.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.taipei.attractions.ClickListener
import com.taipei.attractions.R
import com.taipei.attractions.databinding.AdapterAttractionsAllBinding
import com.taipei.attractions.databinding.ItemLoadingBinding
import com.taipei.attractions.model.AttractionsAllModel

class AttractionsAllAdapter : ListAdapter<AttractionsAllModel.Data, RecyclerView.ViewHolder>(DiffCallback()) {
    private var clickListener: ClickListener<AttractionsAllModel.Data>? = null
    private val viewTypeItem = 0
    private val viewTypeLoading = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewTypeItem == viewType)
            ItemViewHolder.from(parent)
        else
            ItemViewLoadingHolder.from(parent)
    }

    fun setLoadMore(){
        val currentList = currentList.toMutableList()
        currentList.add(null)
        submitList(currentList)
    }

    fun setLoadEnd(){
        val currentList = currentList.toMutableList()
        currentList.removeLast()
        submitList(currentList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(getItem(position))
                holder.itemView.setOnClickListener { v -> clickListener!!.onClick(v, getItem(position), position) }
            }
//            is ItemViewLoadingHolder -> {
//            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position ) == null)
            viewTypeLoading
        else
            viewTypeItem
    }

    fun setOnItemClickListener(clickListener: ClickListener<AttractionsAllModel.Data>) {
        this.clickListener = clickListener
    }

    class ItemViewHolder private constructor(private val binding: AdapterAttractionsAllBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttractionsAllModel.Data) {
            itemView.apply {
                binding.attractionsAll = item
                binding.executePendingBindings()
                if (item.images.isNotEmpty())
                    Glide.with(binding.imgPhoto.context)
                        .load(item.images[0].src + item.images[0].ext)
                        .error(R.mipmap.no_photo)
                        .into(binding.imgPhoto)
                else{
                    Glide.with(binding.imgPhoto.context)
                        .load(R.mipmap.no_photo)
                        .into(binding.imgPhoto)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = AdapterAttractionsAllBinding.inflate(inflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class ItemViewLoadingHolder private constructor(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                return ItemViewLoadingHolder(binding)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AttractionsAllModel.Data>() {

        override fun areContentsTheSame(oldItem: AttractionsAllModel.Data, newItem: AttractionsAllModel.Data): Boolean {
            return oldItem.idData == newItem.idData
        }

        override fun areItemsTheSame(oldItem: AttractionsAllModel.Data, newItem: AttractionsAllModel.Data): Boolean {
            return oldItem.idData == newItem.idData
        }
    }
}



