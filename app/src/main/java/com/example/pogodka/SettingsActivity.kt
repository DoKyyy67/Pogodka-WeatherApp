package com.example.pogodka

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : BaseActivity() {

    private lateinit var tvTemperature: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var tvTimeFormat: TextView
    private lateinit var tvNotifications: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Инициализация View
        tvTemperature = findViewById(R.id.tvTemperatureValue)
        tvWindSpeed = findViewById(R.id.tvWindSpeedValue)
        tvLanguage = findViewById(R.id.tvLanguageValue)
        tvTimeFormat = findViewById(R.id.tvTimeFormatValue)
        tvNotifications = findViewById(R.id.tvNotificationsValue)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Загружаем сохраненные значения
        loadSettings()

        // Навигация
        setupBottomNavigation()

        // Обработчики кликов
        findViewById<CardView>(R.id.cardTemperature).setOnClickListener {
            showTemperatureDialog()
        }
        findViewById<CardView>(R.id.cardWindSpeed).setOnClickListener {
            showWindSpeedDialog()
        }
        findViewById<CardView>(R.id.cardLanguage).setOnClickListener {
            showLanguageDialog()
        }
        findViewById<CardView>(R.id.cardTimeFormat).setOnClickListener {
            showTimeFormatDialog()
        }
        findViewById<CardView>(R.id.cardNotifications).setOnClickListener {
            toggleNotifications()
        }
    }

    private fun loadSettings() {
        // Температура
        tvTemperature.text = if (SettingsManager.getTempUnit(this) == "celsius")
            getString(R.string.celsius) else getString(R.string.fahrenheit)

        // Ветер
        tvWindSpeed.text = if (SettingsManager.getWindUnit(this) == "ms")
            getString(R.string.wind_unit_ms) else getString(R.string.wind_unit_kmh)

        // Язык
        tvLanguage.text = if (SettingsManager.getLanguage(this) == "ru")
            getString(R.string.russian) else getString(R.string.english)

        // Формат времени
        tvTimeFormat.text = if (SettingsManager.is24HourFormat(this))
            getString(R.string.time_24h) else getString(R.string.time_12h)
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
                    startActivity(Intent(this, ForecastActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    true
                }
                else -> false
            }
        }
        bottomNavigation.selectedItemId = R.id.navigation_settings
    }

    private fun showTemperatureDialog() {
        val options = arrayOf(
            getString(R.string.celsius),
            getString(R.string.fahrenheit)
        )
        val currentUnit = SettingsManager.getTempUnit(this)
        val checkedItem = if (currentUnit == "celsius") 0 else 1

        AlertDialog.Builder(this)
            .setTitle(R.string.select_scale)
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                val newUnit = if (which == 0) "celsius" else "fahrenheit"
                SettingsManager.setTempUnit(this, newUnit)
                tvTemperature.text = options[which]
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showWindSpeedDialog() {
        val options = arrayOf(
            getString(R.string.wind_unit_ms),
            getString(R.string.wind_unit_kmh)
        )
        val currentUnit = SettingsManager.getWindUnit(this)
        val checkedItem = if (currentUnit == "ms") 0 else 1

        AlertDialog.Builder(this)
            .setTitle(R.string.select_wind_unit)
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                val newUnit = if (which == 0) "ms" else "kmh"
                SettingsManager.setWindUnit(this, newUnit)
                tvWindSpeed.text = options[which]
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showLanguageDialog() {
        val options = arrayOf(
            getString(R.string.russian),
            getString(R.string.english)
        )
        val currentLang = SettingsManager.getLanguage(this)
        val checkedItem = if (currentLang == "ru") 0 else 1

        AlertDialog.Builder(this)
            .setTitle(R.string.select_language)
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                val newLang = if (which == 0) "ru" else "en"
                SettingsManager.setLanguage(this, newLang)
                tvLanguage.text = options[which]

                // Перезапускаем приложение для применения языка
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showTimeFormatDialog() {
        val options = arrayOf(
            getString(R.string.time_24h),
            getString(R.string.time_12h)
        )
        val currentIs24 = SettingsManager.is24HourFormat(this)
        val checkedItem = if (currentIs24) 0 else 1

        AlertDialog.Builder(this)
            .setTitle(R.string.select_time_format)
            .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                val is24 = which == 0
                SettingsManager.setTimeFormat(this, is24)
                tvTimeFormat.text = options[which]
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun toggleNotifications() {
        val current = tvNotifications.text.toString()
        val newValue = if (current == getString(R.string.notifications_enabled))
            getString(R.string.notifications_disabled)
        else getString(R.string.notifications_enabled)
        tvNotifications.text = newValue
        Toast.makeText(this, getString(R.string.notifications_changed, newValue), Toast.LENGTH_SHORT).show()
    }
}