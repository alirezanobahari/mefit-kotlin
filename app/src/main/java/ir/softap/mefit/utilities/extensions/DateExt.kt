package ir.softap.mefit.utilities.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * extension function to format [Date] as human readable pattern (dd MMMM yy HH:mm)
 */
fun Date.toHumanReadable(locale: Locale): String =
        SimpleDateFormat("dd MMMM yy HH:mm", locale)
        .format(this)
