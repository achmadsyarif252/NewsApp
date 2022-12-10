package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemNewsBinding

class NewsAdapter(private val listBerita: ArrayList<Berita>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.judulBerita.text = listBerita[position].judul
        holder.binding.isiBerita.text = listBerita[position].isi
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listBerita[position])
        }
    }

    override fun getItemCount() = listBerita.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Berita)
    }
}