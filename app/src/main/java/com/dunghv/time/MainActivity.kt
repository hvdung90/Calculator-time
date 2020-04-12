package com.dunghv.time

import android.os.Bundle
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.dunghv.libtime.LocalDateCP
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    //var calendar: CalendarView? = null
    var date = 1620443340000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var ca = Calendar.getInstance()
            ca.set(year, month, dayOfMonth)
            date = ca.timeInMillis
        }
        fab.setOnClickListener { view ->
            val utc = Calendar.getInstance().timeInMillis

            try {
                val formatter =
                    SimpleDateFormat("dd/MM/yyyy HH:mm:00", Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("GMT")
                val start = formatter.format(utc)
                val end = formatter.format(date)
                println("date start $start")
                println("date end $end")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val d = LocalDateCP.between(date)
            txt_result.text =
                "${d.minute} minutes , ${d.hour} hours , ${d.day} days , ${d.month} months , ${d.year} years"
        }
    }

}
