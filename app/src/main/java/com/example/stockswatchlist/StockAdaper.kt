package com.example.stockswatchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockswatchlist.databinding.StockItemBinding

class StockAdapter(
    private var stocks: List<Stock>,
    private val onClickStar: (Stock) -> Unit
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(val binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stock: Stock) {
            binding.stockNameTextView.text = stock.name
            binding.stockPriceTextView.text = String.format("$%.2f", stock.price)
            binding.stockTickerTextView.text = stock.ticker // Make sure you have a TextView with this ID in your layout

            val starIconRes = if (stock.isInWatchlist) R.drawable.ic_star else R.drawable.ic_star_border
            binding.addToWatchlistButton.setImageResource(starIconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StockItemBinding.inflate(layoutInflater, parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stocks[position]
        holder.bind(stock)
        holder.binding.addToWatchlistButton.setOnClickListener {
            onClickStar(stock)
        }
    }


    override fun getItemCount() = stocks.size

    fun updateStocks(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }
}
