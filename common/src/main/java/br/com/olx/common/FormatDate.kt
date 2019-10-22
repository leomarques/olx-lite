package br.com.olx.common

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(date: String): String {
    return try {
        val dateLong = date.toLong()
        val timeStamp = Timestamp(dateLong)
        val stampTime = timeStamp.time
        val stampDate = Date(stampTime)
        val locale = Locale("pt", "BR")
        val sdf = SimpleDateFormat("dd 'de' MMMM 'às' HH:mm", locale)

        sdf.format(stampDate)
    } catch (e: Exception) {
        ""
    }
}