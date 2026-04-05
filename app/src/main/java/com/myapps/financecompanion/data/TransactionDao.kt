package com.myapps.financecompanion.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getByType(type: String): LiveData<List<Transaction>>

    // ── new queries for Home Dashboard ──

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    fun getTotalIncome(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    fun getTotalExpense(): LiveData<Double?>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 5")
    fun getRecentTransactions(): LiveData<List<Transaction>>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = 'expense' GROUP BY category")
    fun getExpenseByCategory(): LiveData<List<CategoryTotal>>

    // ── new queries for Insights ──

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND date >= :startDate AND date <= :endDate")
    fun getExpenseBetweenDates(startDate: Long, endDate: Long): LiveData<Double?>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = 'expense' GROUP BY category ORDER BY total DESC LIMIT 1")
    fun getTopSpendingCategory(): LiveData<CategoryTotal?>

    @Query("SELECT * FROM transactions WHERE type = 'expense' ORDER BY amount DESC LIMIT 3")
    fun getTopExpenses(): LiveData<List<Transaction>>
}


data class CategoryTotal(
    val category: String,
    val total: Double
)

