package com.mauriciobenigno.secureway.helper

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


/*
class NotificationHelper (context: Context) : ContextWrapper(context){
    val context = context

    companion object {
        val SW_CHANNEL = "secureway"
    }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val trackChannel = NotificationChannel(
                SW_CHANNEL,
                "Alerta SecureWay",
                NotificationManager.IMPORTANCE_DEFAULT)

            trackChannel.lightColor = Color.BLUE
            notificationManager.createNotificationChannel(trackChannel)

        } else {

        }
    }

    fun gerarNotificacao(titulo: String, mensagem: String): NotificationCompat.Builder {

        //versões Oreo ou superio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return NotificationCompat
                .Builder(applicationContext, SW_CHANNEL)
                .setOngoing(false)
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setColor(Color.BLUE)
        }

        //versão anteriores ao 8
        return NotificationCompat.Builder(applicationContext)
            .setOngoing(false)
            .setContentTitle(titulo)
            .setColor(Color.BLUE)
            .setCategory(NotificationCompat.CATEGORY_PROMO)
            .setContentText(mensagem)
    }

    fun notify(id: Int, notification: NotificationCompat.Builder) {
        notificationManager.notify(id, notification.build())
    }

    fun remove() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                notificationManager.deleteNotificationChannel(SW_CHANNEL)
            else
                notificationManager.cancelAll()
        } catch (ex: Exception) {
            Log.i("NotificationHelper", ex.message ?: " Falha ao cancelar notificações")
        }
    }
}

*/

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private val CHANNEL_NAME = "Canal de alta prioridade"
    private val CHANNEL_ID = "com.mauriciobenigno.helper$CHANNEL_NAME"
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannels() {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Canal de avisos Secure Way"
        notificationChannel.lightColor = Color.RED
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
    }

    fun sendHighPriorityNotification(title: String?, body: String?, activityName: Class<*>?) {
        val intent = Intent(this, activityName)
        val pendingIntent =
            PendingIntent.getActivity(this, 267, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_delete)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                    NotificationCompat.BigTextStyle().setSummaryText("summary")
                        .setBigContentTitle(title).bigText(body)
                )
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        NotificationManagerCompat.from(this).notify(Random().nextInt(), notification)
    }

    companion object {
        private const val TAG = "NotificationHelper"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }
}