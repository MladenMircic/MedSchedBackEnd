package diplomski.etf.bg.ac.rs.models

import diplomski.etf.bg.ac.rs.models.database_models.Service
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class AppointmentInfo(
    val selectedServicesList: List<Service>,
    val selectedDate: LocalDate,
    val selectedTime: LocalTime
)