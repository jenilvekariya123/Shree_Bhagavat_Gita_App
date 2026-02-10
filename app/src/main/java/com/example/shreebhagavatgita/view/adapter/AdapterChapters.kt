package com.example.shreebhagavatgita.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shreebhagavatgita.Models.ChaptersItems
import com.example.shreebhagavatgita.databinding.ItemChaptersBinding
import kotlin.reflect.KFunction1

class AdapterChapters(
    val onChapterClick: KFunction1<ChaptersItems, Unit>,
    val onFavouriteClick: (ChaptersItems) -> Unit,
    private val showSaveBtn: Boolean
) : RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder>() {

    class ChaptersViewHolder(val binding: ItemChaptersBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ChaptersItems>() {
        override fun areItemsTheSame(oldItem: ChaptersItems, newItem: ChaptersItems): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersItems, newItem: ChaptersItems): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        return ChaptersViewHolder(ItemChaptersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter: ChaptersItems = differ.currentList[position]

        if (!showSaveBtn) {
            holder.binding.apply {
                idFavorite.visibility = View.GONE
                idFillFavorite.visibility = View.GONE
            }
        }


        holder.binding.apply {
            tvChapterNumber.text = "Chapter ${chapter.chapterNumber}"
            tvChapterName.text = "${chapter.nameTranslated}"
            tvChapterContent.text = "${chapter.chapterSummary}"
            tvVerseNumber.text = chapter.versesCount.toString()
        }

        holder.binding.apply {
            idFavorite.setOnClickListener {
                onFavouriteClick(chapter)
            }
        }

        holder.binding.idConsLayout.setOnClickListener{
            onChapterClick(chapter)
        }
    }
}