package com.dev.nagdaadmin.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateAndTimePicker {
    fun showDatePicker(editText: EditText,context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            editText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePicker.show()
    }

    fun showTimePicker(editText: EditText,context: Context) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                selectedTime.set(Calendar.MINUTE, selectedMinute)

                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                editText.setText(timeFormat.format(selectedTime.time))
            },
            hour, minute, false
        )

        timePickerDialog.show()
    }

    fun Long.toArabicDateTime(): String {
        val sdf = SimpleDateFormat("EEEE، d MMMM yyyy - h:mm a", Locale("ar"))
        return sdf.format(Date(this))
    }
    fun Long.toArabicTimeAgo(): String {
        val now = System.currentTimeMillis()
        val diff = now - this

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours   = minutes / 60
        val days    = hours   / 24
        val weeks   = days    / 7
        val months  = days    / 30
        val years   = days    / 365

        return when {
            seconds < 60  -> "منذ لحظات"
            minutes < 60  -> "منذ ${toArabicNumerals(minutes)} ${minuteLabel(minutes)}"
            hours   < 24  -> "منذ ${toArabicNumerals(hours)} ${hourLabel(hours)}"
            days    < 7   -> "منذ ${toArabicNumerals(days)} ${dayLabel(days)}"
            weeks   < 4   -> "منذ ${toArabicNumerals(weeks)} ${weekLabel(weeks)}"
            months  < 12  -> "منذ ${toArabicNumerals(months)} ${monthLabel(months)}"
            else          -> "منذ ${toArabicNumerals(years)} ${yearLabel(years)}"
        }
    }

    private fun toArabicNumerals(number: Long): String {
        return number.toString()
            .replace('0', '٠').replace('1', '١').replace('2', '٢')
            .replace('3', '٣').replace('4', '٤').replace('5', '٥')
            .replace('6', '٦').replace('7', '٧').replace('8', '٨')
            .replace('9', '٩')
    }

    private fun minuteLabel(n: Long) = when {
        n == 1L        -> "دقيقة"
        n == 2L        -> "دقيقتين"
        n in 3..10     -> "دقائق"
        else           -> "دقيقة"
    }

    private fun hourLabel(n: Long) = when {
        n == 1L        -> "ساعة"
        n == 2L        -> "ساعتين"
        n in 3..10     -> "ساعات"
        else           -> "ساعة"
    }

    private fun dayLabel(n: Long) = when {
        n == 1L        -> "يوم"
        n == 2L        -> "يومين"
        n in 3..10     -> "أيام"
        else           -> "يوم"
    }

    private fun weekLabel(n: Long) = when {
        n == 1L        -> "أسبوع"
        n == 2L        -> "أسبوعين"
        n in 3..10     -> "أسابيع"
        else           -> "أسبوع"
    }

    private fun monthLabel(n: Long) = when {
        n == 1L        -> "شهر"
        n == 2L        -> "شهرين"
        n in 3..10     -> "أشهر"
        else           -> "شهر"
    }

    private fun yearLabel(n: Long) = when {
        n == 1L        -> "سنة"
        n == 2L        -> "سنتين"
        n in 3..10     -> "سنوات"
        else           -> "سنة"
    }
}