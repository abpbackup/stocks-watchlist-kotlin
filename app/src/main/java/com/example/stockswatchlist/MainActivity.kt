package com.example.stockswatchlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stockswatchlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val stockAdapter = StockAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stocksRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)
            adapter = stockAdapter
        }

        // TODO: Set up the SearchView listener and implement the search functionality
    }
}
