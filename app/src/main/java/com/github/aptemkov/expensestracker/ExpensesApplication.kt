package com.github.aptemkov.expensestracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import com.github.aptemkov.expensestracker.domain.transaction.TransactionRoomDatabase
import com.github.aptemkov.expensestracker.notification.CounterNotificationService

class ExpensesApplication : Application() {
    val database: TransactionRoomDatabase by lazy { TransactionRoomDatabase.getDatabase(this)}


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CounterNotificationService.COUNTER_CHANNEL_ID,
            "Counter",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for the increment counter notifications"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
