package diplomski.etf.bg.ac.rs.models.notificaton_data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
sealed class NotificationData {

    abstract val type: NotificationType

    @Serializable
    data class PatientAppointmentScheduleData(
        @SerialName("patient_name")
        val patientName: String,
        @SerialName("date_of_action")
        val dateOfAction: LocalDate,
        @SerialName("time_of_action")
        val timeOfAction: LocalTime
    ) : NotificationData() {
        @SerialName("notification_type")
        override val type: NotificationType = NotificationType.SCHEDULED
    }

    @Serializable
    data class PatientAppointmentCancelData(
        @SerialName("patient_name")
        val patientName: String,
        @SerialName("date_of_action")
        val dateOfAction: LocalDate,
        @SerialName("time_of_action")
        val timeOfAction: LocalTime
    ) : NotificationData() {
        @SerialName("notification_type")
        override val type: NotificationType = NotificationType.CANCELLED
    }

    @Serializable
    data class DoctorAppointmentCancelData(
        @SerialName("doctor_name")
        val doctorName: String,
        @SerialName("date_of_action")
        val dateOfAction: LocalDate,
        @SerialName("time_of_action")
        val timeOfAction: LocalTime
    ) : NotificationData() {
        @SerialName("notification_type")
        override val type: NotificationType = NotificationType.CANCELLED
    }

    enum class NotificationType {
        SCHEDULED, CANCELLED
    }
}