package com.example.shreebhagavatgita.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.shreebhagavatgita.databinding.ItemVerseBinding

class AdapterVerses(val onVerseItemClick: (String, Int) -> Unit) : RecyclerView.Adapter<AdapterVerses.VersesViewHolder>() {

    class VersesViewHolder(val binding: ItemVerseBinding):ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VersesViewHolder {
        return VersesViewHolder(ItemVerseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VersesViewHolder, position: Int) {
        val verses = differ.currentList[position]

        holder.binding.tvVerseNo.text = "Verse ${position + 1}"
        holder.binding.tvVerseContent.text = verses

        holder.binding.idLiner.setOnClickListener {

               onVerseItemClick(verses,position + 1)

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}