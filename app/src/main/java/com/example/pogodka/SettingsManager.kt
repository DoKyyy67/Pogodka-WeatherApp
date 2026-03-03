package com.example.pogodka

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {

    private const val PREFS_NAME = "app_settings"
    private const val KEY_TEMP_UNIT = "temp_unit"
    private const val KEY_WIND_UNIT = "wind_unit"
    private const val KEY_TIME_FORMAT = "time_format"
    private const val KEY_LANGUAGE = "language"

    fun getPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Температура
    fun getTempUnit(context: Context): String =
        getPreferences(context).getString(KEY_TEMP_UNIT, "celsius") ?: "celsius"

    fun setTempUnit(context: Context, unit: String) {
        getPreferences(context).edit().putString(KEY_TEMP_UNIT, unit).apply()
    }

    // Ветер
    fun getWindUnit(context: Context): String =
        getPreferences(context).getString(KEY_WIND_UNIT, "ms") ?: "ms"

    fun setWindUnit(context: Context, unit: String) {
        getPreferences(context).edit().putString(KEY_WIND_UNIT, unit).apply()
    }

    // Формат времени
    fun is24HourFormat(context: Context): Boolean =
        getPreferences(context).getBoolean(KEY_TIME_FORMAT, true)

    fun setTimeFormat(context: Context, is24Hour: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_TIME_FORMAT, is24Hour).apply()
    }

    // Язык
    fun getLanguage(context: Context): String =
        getPreferences(context).getString(KEY_LANGUAGE, "ru") ?: "ru"

    fun setLanguage(context: Context, lang: String) {
        getPreferences(context).edit().putString(KEY_LANGUAGE, lang).apply()
    }

    // Конвертация температуры
    fun convertTemperature(value: Int, fromUnit: String = "celsius", toUnit: String): Int {
        return if (fromUnit == "celsius" && toUnit == "fahrenheit") {
            (value * 9 / 5) + 32
        } else if (fromUnit == "fahrenheit" && toUnit == "celsius") {
            (value - 32) * 5 / 9
        } else {
            value
        }
    }

    // Конвертация скорости ветра (м/с <-> км/ч)
    fun convertWindSpeed(value: Int, fromUnit: String = "ms", toUnit: String): Int {
        return if (fromUnit == "ms" && toUnit == "kmh") {
            (value * 3.6).toInt()
        } else if (fromUnit == "kmh" && toUnit == "ms") {
            (value / 3.6).toInt()
        } else {
            value
        }
    }
}