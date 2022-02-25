package com.tool.scanqr.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.tool.scanqr.MainApplication
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class StoreDataHelper private constructor() {
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance())
    private val edit: SharedPreferences.Editor = sharedPreferences.edit()

    private val TIME_LEFT = "time_left"
    private val DATE_FORMAT = "dd-MM-yyyy"

    private object Holder {
        val INSTANCE = StoreDataHelper()
    }

    companion object {
        @JvmStatic
        fun getInstance(): StoreDataHelper {
            return Holder.INSTANCE
        }
    }

    fun addTimeLefts(time: Int) {
        val today = LocalDate.now()

        val saveDate = sharedPreferences.getString(TIME_LEFT, today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
        val localDate = LocalDate.parse(saveDate, DateTimeFormatter.ofPattern(DATE_FORMAT))
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, time)


        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        edit.putString(TIME_LEFT, format.format(calendar.time))
        edit.commit()
    }

    fun getDayLefts(): Int {
        val today = LocalDate.now()
        val toDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val saveDate = sharedPreferences.getString(TIME_LEFT, today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
        val localDate = LocalDate.parse(saveDate, DateTimeFormatter.ofPattern(DATE_FORMAT))
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())



        val diff = date.time - toDate.time

        val timeUnit = TimeUnit.DAYS

        return timeUnit.convert(diff, TimeUnit.MILLISECONDS).toInt()
    }


}