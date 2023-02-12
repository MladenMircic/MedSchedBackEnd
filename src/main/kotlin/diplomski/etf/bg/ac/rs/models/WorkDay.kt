package diplomski.etf.bg.ac.rs.models

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class WorkDay(
    val dayOfWeek: DayOfWeek,
    val workHours: List<LocalTime>
)