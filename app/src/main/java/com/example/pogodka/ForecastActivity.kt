package com.example.pogodka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class ForecastActivity : BaseActivity() {

    private lateinit var btnWeek: Button
    private lateinit var btnDay: Button
    private lateinit var forecastContainer: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    // Исходные данные
    private val weekTemperatures = arrayOf(-5, -3, -1, 2, 5, 3, 1)
    private val dayTemperatures = arrayOf(-5, -3, -1, -2, -4, -6)
    private val weekDays = arrayOf("пн", "вт", "ср", "чт", "пт", "сб", "вс")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        btnWeek = findViewById(R.id.btnWeek)
        btnDay = findViewById(R.id.btnDay)
        forecastContainer = findViewById(R.id.forecastContainer)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        setupBottomNavigation()

        btnWeek.setOnClickListener {
            showWeekForecast()
            btnWeek.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_active_bg))
            btnDay.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        btnDay.setOnClickListener {
            showDayForecast()
            btnDay.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_active_bg))
            btnWeek.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }

        // Показываем недельный прогноз по умолчанию
        showWeekForecast()
        btnWeek.setBackgroundColor(ContextCompat.getColor(this, R.color.nav_active_bg))
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_forecast -> {
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigation.selectedItemId = R.id.navigation_forecast
    }

    private fun showWeekForecast() {
        forecastContainer.removeAllViews()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale(SettingsManager.getLanguage(this)))
        val tempUnit = SettingsManager.getTempUnit(this)

        for (i in weekDays.indices) {
            val tempCalendar = calendar.clone() as Calendar
            tempCalendar.add(Calendar.DAY_OF_YEAR, i)

            val displayedTemp = if (tempUnit == "celsius") weekTemperatures[i]
            else SettingsManager.convertTemperature(weekTemperatures[i], "celsius", "fahrenheit")

            val itemView = layoutInflater.inflate(R.layout.item_forecast, forecastContainer, false)
            val dayText = itemView.findViewById<TextView>(R.id.dayText)
            val tempText = itemView.findViewById<TextView>(R.id.tempText)

            dayText.text = "${weekDays[i]} - ${dateFormat.format(tempCalendar.time)}"
            tempText.text = "$displayedTemp°"

            tempText.setTextColor(
                if (weekTemperatures[i] < 0) ContextCompat.getColor(this, R.color.blue_accent)
                else ContextCompat.getColor(this, R.color.warm_orange)
            )

            forecastContainer.addView(itemView)
        }
    }

    private fun showDayForecast() {
        forecastContainer.removeAllViews()
        val timeFormat = if (SettingsManager.is24HourFormat(this)) "HH:mm" else "h:mm a"
        val sdf = SimpleDateFormat(timeFormat, Locale(SettingsManager.getLanguage(this)))
        val tempUnit = SettingsManager.getTempUnit(this)

        val times = arrayOf(8, 11, 14, 17, 20, 23)

        for (i in times.indices) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, times[i])
            calendar.set(Calendar.MINUTE, 0)

            val displayedTemp = if (tempUnit == "celsius") dayTemperatures[i]
            else SettingsManager.convertTemperature(dayTemperatures[i], "celsius", "fahrenheit")

            val itemView = layoutInflater.inflate(R.layout.item_forecast, forecastContainer, false)
            val timeText = itemView.findViewById<TextView>(R.id.dayText)
            val tempText = itemView.findViewById<TextView>(R.id.tempText)

            timeText.text = sdf.format(calendar.time)
            tempText.text = "$displayedTemp°"

            tempText.setTextColor(
                if (dayTemperatures[i] < 0) ContextCompat.getColor(this, R.color.blue_accent)
                else ContextCompat.getColor(this, R.color.warm_orange)
            )

            forecastContainer.addView(itemView)
        }
    }
}