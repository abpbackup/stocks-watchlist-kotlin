package com.example.stockswatchlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockswatchlist.databinding.ActivityMainBinding
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var stockAdapter: StockAdapter
    private var stocks: List<Stock> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stockAdapter = StockAdapter(emptyList())
        binding.stocksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.stocksRecyclerView.adapter = stockAdapter

        loadMockData()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterStocks(it)
                }
                // It's a good practice to collapse the keyboard when the user submits the query
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterStocks(it)
                }
                return true
            }
        })
    }

    private fun loadMockData() {
        val json = getJsonDataFromAsset("stocks.json")
        json?.let {
            val listType = object : TypeToken<List<Stock>>() {}.type
            stocks = Gson().fromJson(it, listType)
            stockAdapter.updateStocks(stocks)
        }
    }

    private fun filterStocks(query: String) {
        val filteredList = if (query.isNotEmpty()) {
            stocks.filter {
                it.name.contains(query, ignoreCase = true) || it.ticker.contains(query, ignoreCase = true)
            }
        } else {
            stocks
        }
        stockAdapter.updateStocks(filteredList)
    }


    private fun getJsonDataFromAsset(fileName: String): String? {
        return try {
            assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
