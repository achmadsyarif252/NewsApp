package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityDetailBeritaBinding

class DetailBerita : AppCompatActivity() {
    private var _binding: ActivityDetailBeritaBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBeritaBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val news = intent.getParcelableExtra<Berita>(EXTRA_NEWS)
        tampilkanBerita(news ?: Berita())

        supportActionBar?.title = "Detail Berita"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun tampilkanBerita(news: Berita) {
        binding?.media?.text = news.media
        binding?.judul?.text = news.judul
        binding?.author?.text = news.author
        binding?.publishedAt?.text = news.publishedAt
        var content = ""
        //akses content dibatasi ole news.api ,perulangan untu memperpanjang konten
        for (i in 0..10) {
            content += news.isi
        }
        binding?.isiBerita?.text = content
        binding?.gambar?.let { Glide.with(this).load(news.imageUrl).into(it) }
    }

    companion object {
        const val EXTRA_NEWS = "EXTRA_NEWS"
    }
}