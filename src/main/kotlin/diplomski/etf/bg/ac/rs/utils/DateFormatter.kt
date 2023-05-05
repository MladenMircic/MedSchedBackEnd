package diplomski.etf.bg.ac.rs.utils

import kotlinx.datetime.LocalDate
import java.util.*

class DateFormatter {
    companion object {
        fun dateAsString(date: LocalDate): String =
            String.format(
                "%s %d, %d",
                date.month.name.lowercase()
                    .replaceFirstChar { char ->
                        if (char.isLowerCase())
                            char.titlecase(Locale.getDefault())
                        else
                            char.toString()
                    }
                    .substring(0, 3),
                date.dayOfMonth,
                date.year
            )
    }
}