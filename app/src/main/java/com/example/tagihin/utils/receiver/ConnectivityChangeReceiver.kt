package com.example.tagihin.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class ConnectivityChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (com.example.tagihin.utils.ConnectivityManager.isOnline(context)) {
                //Toast.makeText(context,"You're online",Toast.LENGTH_SHORT).show()
            }else{
                //Toast.makeText(context,"You're offline",Toast.LENGTH_SHORT).show()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

}