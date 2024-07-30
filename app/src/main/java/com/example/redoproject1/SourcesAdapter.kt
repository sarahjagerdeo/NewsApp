package com.example.redoproject1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SourcesAdapter : ListAdapter<NewsSource, SourcesAdapter.SourceViewHolder>(SourceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_source, parent, false)
        return SourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val source = getItem(position)
        holder.bind(source)
    }

    inner class SourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(source: NewsSource) {
            nameTextView.text = source.name
            descriptionTextView.text = source.description
        }
    }

    class SourceDiffCallback : DiffUtil.ItemCallback<NewsSource>() {
        override fun areItemsTheSame(oldItem: NewsSource, newItem: NewsSource): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsSource, newItem: NewsSource): Boolean {
            return oldItem == newItem
        }
    }
}
