package com.example.pogodka

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var tvDate: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvCondition: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvTempUnit: TextView
    private val originalTemp = -5
    private val originalWind = 12
    private val originalHumidity = 75
    private val originalPressure = 1013

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDate = findViewById(R.id.textViewDate)
        tvTemperature = findViewById(R.id.textViewTemperature)
        tvCondition = findViewById(R.id.textViewCondition)
        tvHumidity = findViewById(R.id.textViewHumidity)
        tvWind = findViewById(R.id.textViewWind)
        tvPressure = findViewById(R.id.textViewPressure)
        tvTempUnit = findViewById(R.id.textViewTempUnit)

        updateUI()

        findViewById<Button>(R.id.buttonCalendar).setOnClickListener {
            openCalendar()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    Toast.makeText(this, getString(R.string.main_screen), Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_forecast -> {
                    startActivity(Intent(this, ForecastActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateUI() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale(SettingsManager.getLanguage(this)))
        tvDate.text = dateFormat.format(calendar.time)

        val tempUnit = SettingsManager.getTempUnit(this)
        val displayedTemp = if (tempUnit == "celsius") originalTemp
        else SettingsManager.convertTemperature(originalTemp, "celsius", "fahrenheit")
        tvTemperature.text = "$displayedTemp°"

        tvTempUnit.text = if (tempUnit == "celsius") "°C" else "°F"

        val windUnit = SettingsManager.getWindUnit(this)
        val displayedWind = if (windUnit == "ms") originalWind
        else SettingsManager.convertWindSpeed(originalWind, "ms", "kmh")
        val windUnitText = if (windUnit == "ms") getString(R.string.wind_unit_ms) else getString(R.string.wind_unit_kmh)
        tvWind.text = getString(R.string.wind_value, displayedWind, windUnitText)

        tvHumidity.text = getString(R.string.humidity_value, originalHumidity)
        tvPressure.text = getString(R.string.pressure_value, originalPressure)
        tvCondition.text = getString(R.string.condition)
    }

    private fun openCalendar() {
        val calendar = Calendar.getInstance()

        val googleCalendarIntent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("content://com.android.calendar/time/" + calendar.timeInMillis))
            .setPackage("com.google.android.calendar")

        val defaultCalendarIntent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("content://com.android.calendar/time/" + calendar.timeInMillis))

        when {
            googleCalendarIntent.resolveActivity(packageManager) != null -> {
                startActivity(googleCalendarIntent)
            }
            defaultCalendarIntent.resolveActivity(packageManager) != null -> {
                startActivity(defaultCalendarIntent)
            }
            else -> {
                Toast.makeText(this, getString(R.string.calendar_not_found), Toast.LENGTH_LONG).show()
            }
        }
    }
}