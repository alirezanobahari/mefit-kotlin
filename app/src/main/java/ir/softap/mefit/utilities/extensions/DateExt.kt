package ir.softap.mefit.utilities.extensions

import android.content.Context
import ir.softap.mefit.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * extension function to format [Date] as human readable pattern (dd MMMM yy HH:mm)
 */
fun Date.toHumanReadable(locale: Locale): String =
    SimpleDateFormat("dd MMMM yy HH:mm", locale)
        .format(this)

fun Date.calculateDifference(context: Context): String {
    val nowTimeStamp = Date().time
    val dateTimeStamp = this.time
    val differenceTimeStamp = nowTimeStamp - dateTimeStamp

    if (differenceTimeStamp == 0L)
        return context.strings[R.string.format_date_minute].format(0)

    val minutes = differenceTimeStamp / 60_000
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = weeks / 4
    val years = months / 12

    return when {
        years >= 1 -> context.strings[R.string.format_date_year].format(years)
        months >= 1 -> context.strings[R.string.format_date_month].format(months)
        weeks >= 1 -> context.strings[R.string.format_date_week].format(weeks)
        days >= 1 -> context.strings[R.string.format_date_day].format(days)
        hours >= 1 -> context.strings[R.string.format_date_hour].format(hours)
        else -> context.strings[R.string.format_date_minute].format(minutes)
    }
}