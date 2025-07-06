package com.example.md_parser.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object WebClient {

     suspend fun doRequest(url: String): String? {

         return withContext(Dispatchers.IO) {
             val connection = URL(url).openConnection() as HttpURLConnection
              try {
                 connection.connect()
                 if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                     Log.d("Request error", "Ошибка подключения: ${connection.responseCode}")
                     null
                 }

                 val inputStream: InputStream = BufferedInputStream(connection.inputStream)
                 inputStream.bufferedReader().use { it.readText() }

             } catch (e: Exception) {
                  Log.d("Request error", "Ошибка: ${e.message}")
                 null
             } finally {
                 connection.disconnect()
             }
         }

    }
}