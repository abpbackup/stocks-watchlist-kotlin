package com.example.stockswatchlist

import android.os.Bundle
import android.view.View
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
    private lateinit var starredStockAdapter: StockAdapter
    private val starredStocks = mutableListOf<Stock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stockAdapter = StockAdapter(emptyList()) { stock -> toggleStockInWatchlist(stock) }
        binding.stocksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.stocksRecyclerView.adapter = stockAdapter

        setupSearchView()

        // Initialize the starred stocks adapter and RecyclerView
        starredStockAdapter = StockAdapter(starredStocks, onClickStar = { stock ->
            toggleStockInWatchlist(stock)
        })
        binding.starredStocksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.starredStocksRecyclerView.adapter = starredStockAdapter

        loadMockData()

    }

    // This function is called from the adapter when the star is clicked
    private fun toggleStockInWatchlist(stock: Stock) {
        val indexInMainList = stocks.indexOfFirst { it.ticker == stock.ticker }
        val indexInStarredList = starredStocks.indexOfFirst { it.ticker == stock.ticker }

        stock.isInWatchlist = !stock.isInWatchlist

        if (stock.isInWatchlist) {
            if (indexInStarredList == -1) {
                starredStocks.add(stock)
            }
        } else {
            if (indexInStarredList != -1) {
                starredStocks.removeAt(indexInStarredList)
            }
        }

        starredStockAdapter.updateStocks(starredStocks)
        if (indexInMainList != -1) {
            stockAdapter.notifyItemChanged(indexInMainList)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterStocks(it)
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    if (query.isNotEmpty()) {
                        filterStocks(query)
                        binding.stocksRecyclerView.visibility = View.VISIBLE
                        binding.starredStocksRecyclerView.visibility = View.GONE
                    } else {
                        binding.stocksRecyclerView.visibility = View.GONE
                        binding.starredStocksRecyclerView.visibility = View.VISIBLE
                    }
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
        }
    }

    private fun filterStocks(query: String) {
        if (query.isNotEmpty()) {
            val filteredList = stocks.filter {
                it.name.contains(query, ignoreCase = true) || it.ticker.contains(
                    query,
                    ignoreCase = true
                )
            }
            stockAdapter.updateStocks(filteredList)
        } else {
            // If the search query is empty, clear the search results
            stockAdapter.updateStocks(emptyList())
        }
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
