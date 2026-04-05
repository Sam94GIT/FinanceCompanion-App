package com.myapps.financecompanion.repository


import com.myapps.financecompanion.data.AppDatabase
import com.myapps.financecompanion.data.Transaction
import android.content.Context

class TransactionRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).transactionDao()

    val allTransactions = dao.getAllTransactions()

    suspend fun insert(transaction: Transaction) = dao.insert(transaction)
    suspend fun update(transaction: Transaction) = dao.update(transaction)
    suspend fun delete(transaction: Transaction) = dao.delete(transaction)
    fun getByType(type: String) = dao.getByType(type)

    // new for Home Dashboard
    val totalIncome = dao.getTotalIncome()
    val totalExpense = dao.getTotalExpense()
    val recentTransactions = dao.getRecentTransactions()
    val expenseByCategory = dao.getExpenseByCategory()

    // new for Insights
    val topSpendingCategory = dao.getTopSpendingCategory()
    val topExpenses = dao.getTopExpenses()

    fun getExpenseBetweenDates(startDate: Long, endDate: Long) =
        dao.getExpenseBetweenDates(startDate, endDate)
}
