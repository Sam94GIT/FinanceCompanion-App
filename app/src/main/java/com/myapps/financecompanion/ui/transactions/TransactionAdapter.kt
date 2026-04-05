package com.myapps.financecompanion.ui.transactions



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapps.financecompanion.data.Transaction
import com.myapps.financecompanion.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private val onDeleteClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions = listOf<Transaction>()

    fun submitList(list: List<Transaction>) {
        transactions = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {

            binding.tvCategory.text = transaction.category
            binding.tvNote.text = if (transaction.note.isEmpty()) "No note" else transaction.note

            // Format date
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(transaction.date))

            // Amount color and sign
            if (transaction.type == "income") {
                binding.tvAmount.text = "+₹${"%.2f".format(transaction.amount)}"
                binding.tvAmount.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
            } else {
                binding.tvAmount.text = "-₹${"%.2f".format(transaction.amount)}"
                binding.tvAmount.setTextColor(android.graphics.Color.parseColor("#C62828"))
            }

            // Category emoji icon
            binding.tvCategoryIcon.text = getCategoryEmoji(transaction.category)

            // Long press to delete
            binding.root.setOnLongClickListener {
                onDeleteClick(transaction)
                true
            }
        }
    }

    private fun getCategoryEmoji(category: String): String {
        return when (category) {
            "Food" -> "🍔"
            "Transport" -> "🚗"
            "Shopping" -> "🛍️"
            "Bills" -> "📄"
            "Entertainment" -> "🎬"
            "Health" -> "💊"
            "Education" -> "📚"
            "Salary" -> "💰"
            else -> "💳"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size
}