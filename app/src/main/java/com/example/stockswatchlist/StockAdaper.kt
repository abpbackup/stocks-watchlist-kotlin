package com.example.stockswatchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockswatchlist.databinding.StockItemBinding

class StockAdapter(private var stocks: List<Stock>) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(private val binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock) {
            binding.stockNameTextView.text = stock.name
            binding.stockPriceTextView.text = String.format("$%.2f", stock.price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StockItemBinding.inflate(layoutInflater, parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount() = stocks.size

    fun updateStocks(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }
}
